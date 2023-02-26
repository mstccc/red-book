package com.achong.service.impl;

import com.achong.base.BaseInfoProperties;
import com.achong.base.RabbitMQConfig;
import com.achong.enums.MessageEnum;
import com.achong.enums.YesOrNo;
import com.achong.mapper.FansMapper;
import com.achong.mapper.FansMapperCustom;
import com.achong.mo.MessageMO;
import com.achong.pojo.Fans;
import com.achong.pojo.Users;
import com.achong.service.FansService;
import com.achong.service.MsgService;
import com.achong.utils.JsonUtils;
import com.achong.utils.PagedGridResult;
import com.achong.vo.FansVo;
import com.achong.vo.VlogerVo;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FansServiceImpl extends BaseInfoProperties implements FansService {

    @Autowired
    private FansMapper fansMapper;

    @Autowired
    private FansMapperCustom fansMapperCustom;

    @Autowired
    private Sid sid;

    @Autowired
    private MsgService msgService;

    @Autowired
    public RabbitTemplate rabbitTemplate;

    //关注
    @Transactional
    @Override
    public void doFollow(String myId, String vlogerId) {
        String fid = sid.nextShort();

        Fans fans = new Fans();
        fans.setId(fid);
        fans.setFanId(myId);
        fans.setVlogerId(vlogerId);

        //判断对方是否关注我，如果关注我，那么双方都要互为朋友关系
        Fans vloger = queryFansRelationShip(vlogerId, myId);    //
        if (vloger != null){
            fans.setIsFanFriendOfMine(YesOrNo.YES.type);
            vloger.setIsFanFriendOfMine(YesOrNo.YES.type );
            fansMapper.updateByPrimaryKeySelective(vloger);
        }else {
            fans.setIsFanFriendOfMine(YesOrNo.NO.type);
        }

        fansMapper.insert(fans);

        //系统消息：关注
//        msgService.createMsg(myId, vlogerId, MessageEnum.FOLLOW_YOU.type, null);
        MessageMO messageMO = new MessageMO();
        messageMO.setFromUserId(myId);
        messageMO.setToUserId(vlogerId);
        // 优化: 使用mq异步解耦
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_MSG,
                            "sys.msg."+MessageEnum.FOLLOW_YOU.enValue,
                                        JsonUtils.objectToJson(messageMO));
    }

    /**
     * 查询是否互相关注
     */
    public Fans queryFansRelationShip(String fanId, String vlogerId){
        Example example = new Example(Fans.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("vlogerId", vlogerId);
        criteria.andEqualTo("fanId", fanId);

        List list = fansMapper.selectByExample(example);

        Fans fan = null;
        if (list != null && list.size() > 0 && !list.isEmpty()){
            fan = (Fans) list.get(0);
        }
        return fan;
    }


    /**
     * 取消关注
     */
    @Override
    public void doCancel(String myId, String vlogerId) {

        //判断我们是否朋友关系，如果是，则需要取消双方的关系
        Fans fan = queryFansRelationShip(myId, vlogerId);
        if (fan != null && fan.getIsFanFriendOfMine() == YesOrNo.YES.type){
            //抹除双方的朋友关系，自己的关系即可删除
            Fans pendingFan = queryFansRelationShip(vlogerId, myId);
            pendingFan.setIsFanFriendOfMine(YesOrNo.NO.type);
            fansMapper.updateByPrimaryKeySelective(pendingFan);
        }

        //删除自己的关注关联表记录
        fansMapper.delete(fan);

    }

    @Override
    public boolean queryDoIFollowVloger(String myId, String vlogerId) {
        Fans vloger = queryFansRelationShip(myId, vlogerId);
        return vloger != null;
    }

    /**
     * 查询当前用户关注列表
     */
    @Override
    public PagedGridResult queryMyFollows(String myId, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("myId", myId);

        PageHelper.startPage(page, pageSize);

        List<VlogerVo> list = fansMapperCustom.queryMyFollows(map);
        return setterPagedGrid(list, page);
    }

    /**
     * 查询当前用户粉丝列表
     */
    @Override
    public PagedGridResult queryMyFans(String myId, Integer page, Integer pageSize) {

        /**
         * 互粉标记
         *粉丝列表页面：查询我是否关注过该粉丝
         * 1. 事先准备：关联关系保存在redis中，不要依赖数据库
         * 2. 数据库查询后，直接循环查询redis，避免第二次查询数据库的尴尬局面
         */

        Map<String, Object> map = new HashMap<>();
        map.put("myId", myId);

        PageHelper.startPage(page, pageSize);

        List<FansVo> list = fansMapperCustom.queryMyFans(map);

        for (FansVo f : list){
            String relationship = redis.get(REDIS_FANS_AND_VLOGER_RELATIONHiP);
            if (StringUtils.isNotBlank(relationship) && relationship.equalsIgnoreCase("1")){
                f.setFried(true);
            }
        }

        return setterPagedGrid(list, page);
    }
}
