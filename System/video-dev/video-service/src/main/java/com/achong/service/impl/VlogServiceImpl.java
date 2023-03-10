package com.achong.service.impl;

import com.achong.base.BaseInfoProperties;
import com.achong.base.RabbitMQConfig;
import com.achong.bo.VlogBo;
import com.achong.enums.MessageEnum;
import com.achong.enums.YesOrNo;
import com.achong.mapper.MyLikedVlogMapper;
import com.achong.mapper.VlogMapper;
import com.achong.mapper.VlogMapperCustom;
import com.achong.mo.MessageMO;
import com.achong.pojo.MyLikedVlog;
import com.achong.pojo.Vlog;
import com.achong.service.FansService;
import com.achong.service.MsgService;
import com.achong.service.VlogService;
import com.achong.utils.JsonUtils;
import com.achong.utils.PagedGridResult;
import com.achong.vo.IndexVlogVO;
import com.github.pagehelper.PageHelper;
import net.sf.jsqlparser.statement.create.table.Index;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VlogServiceImpl extends BaseInfoProperties implements VlogService {

    @Autowired
    private Sid sid;

    @Autowired
    private VlogMapper vlogMapper;


    @Autowired
    private VlogMapperCustom vlogMapperCustom;

    @Autowired
    private MyLikedVlogMapper myLikedVlogMapper;

    @Autowired
    private FansService fansService;

    @Autowired
    private MsgService msgService;

    @Autowired
    public RabbitTemplate rabbitTemplate;



    @Transactional
    @Override
    public void createVlog(VlogBo vlogBo) {
        String vid = sid.nextShort();

        Vlog vlog = new Vlog();
        BeanUtils.copyProperties(vlogBo, vlog);

        vlog.setId(vid);

        vlog.setLikeCounts(0);
        vlog.setCommentsCounts(0);
        vlog.setIsPrivate(YesOrNo.NO.type);

        vlog.setCreatedTime(new Date());
        vlog.setUpdatedTime(new Date());

        vlogMapper.insert(vlog);
    }

    /**
     *  ?????????????????????????????????
     * @param search ??????????????????????????????????????????
     * @return
     */
    @Override
    public PagedGridResult getIndexVlogList(String userId,
                                            String search,
                                            Integer page,
                                            Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(search)){
            map.put("search", search);
        }
        List<IndexVlogVO> list = vlogMapperCustom.getIndexVlogList(map);

        for (IndexVlogVO v : list) {
            String vlogerId = v.getVlogerId();
            String vlogId = v.getVlogId();

            if (StringUtils.isNotBlank(userId)) {
                // ???????????????????????????
                boolean doIFollowVloger = fansService.queryDoIFollowVloger(userId, vlogerId);
                v.setDoIFollowVloger(doIFollowVloger);

                // ???????????????????????????????????????
                v.setDoILikeThisVlog(doILikeVlog(userId, vlogId));
            }

            // ???????????????????????????????????????
            v.setLikeCounts(getVlogBeLikedCounts(vlogId));
        }

        return setterPagedGrid(list, page);
    }
    private boolean doILikeVlog(String myId, String vlogId){
        String doILike = redis.get(REDIS_USER_LIKE_VLOG + ":" + myId + ":" + vlogId);
        boolean isLike = false;
        if (StringUtils.isNotBlank(doILike) && doILike.equalsIgnoreCase("1")){
            isLike = true;
        }
        return isLike;
    }

    /**
     * ???????????????????????????
     */
    private IndexVlogVO setterVo(IndexVlogVO v, String userId){
        String vlogerId = v.getVlogerId();
        String vlogId = v.getVlogId();


        if (StringUtils.isNotBlank(userId)){
            //???????????????????????????
            boolean doIFollowVloger = fansService.queryDoIFollowVloger(userId, vlogerId);
            v.setDoIFollowVloger(doIFollowVloger);

            //???????????????????????????????????????
            v.setDoILikeThisVlog(doILikeVlog(userId, vlogId));
        }
        //???????????????????????????????????????
        v.setLikeCounts(getVlogBeLikedCounts(vlogId));

        return v;
    }

    //?????????????????????????????????
    @Override
    public Integer getVlogBeLikedCounts(String vlogId){
        String countsStr = redis.get(REDIS_VLOG_BE_LIKED_COUNTS + ":"  +vlogId);
        if (StringUtils.isBlank(countsStr)){
            countsStr = "0";
        }
        return Integer.valueOf(countsStr);
    }

    @Override
    public IndexVlogVO getVlogDetailById(String userId, String vlogId) {
        Map<String, Object> map = new HashMap<>();
        map.put("vlogId", vlogId);
        List<IndexVlogVO> list = vlogMapperCustom.getVlogDetailById(map);
        if (list != null && list.size() > 0){
            IndexVlogVO vlogVO = list.get(0);

            return setterVo(vlogVO, userId);
        }
        return null;
    }

    @Transactional
    @Override
    public void changeToPrivateOrPublic(String userId, String vlogId, Integer yesOrNo) {

        Example example = new Example(Vlog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", vlogId);
        criteria.andEqualTo("vlogerId", userId);

        Vlog pendingVlog = new Vlog();
        pendingVlog.setIsPrivate(yesOrNo);

        vlogMapper.updateByExampleSelective(pendingVlog, example);
    }

    @Override
    public PagedGridResult queryMyVlogList(String userId, Integer page, Integer pageSize, Integer yesOrNo) {

        Example example = new Example(Vlog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("vlogerId", userId);
        criteria.andEqualTo("isPrivate", yesOrNo);

        PageHelper.startPage(page, pageSize);
        List<Vlog> list = vlogMapper.selectByExample(example);

        return setterPagedGrid(list, page);
    }

    /**
     * ??????????????????
     */
    @Transactional
    @Override
    public void userLikeVlog(String userId, String vlogId, String vlogerId) {

        String rid = sid.nextShort();

        //???????????????????????????????????????????????????
        MyLikedVlog myLikedVlog = new MyLikedVlog();
        myLikedVlog.setId(rid);
        myLikedVlog.setVlogId(vlogId);
        myLikedVlog.setUserId(userId);

        myLikedVlogMapper.insert(myLikedVlog);

        //???????????????????????????????????????????????????+1
        redis.increment(REDIS_VLOGER_BE_LIKED_COUNTS+":"+vlogerId, 1);
        redis.increment(REDIS_VLOG_BE_LIKED_COUNTS+":"+vlogId, 1);

        //??????????????????????????????redis????????? ?????? ??? ?????? ???????????????
        redis.set(REDIS_USER_LIKE_VLOG+":"+userId+":"+vlogId, "1");

        // ??????????????????????????????
//        Vlog vlog = this.getVlog(vlogId);
//        Map msgContent = new HashMap();
//        msgContent.put("vlogId", vlogId);
//        msgContent.put("vlogCover", vlog.getCover());
//        msgService.createMsg(userId,
//                            vlog.getVlogerId(),
//                            MessageEnum.LIKE_VLOG.type,
//                            msgContent);

        // ??????????????????????????????????????????mq????????????
        MessageMO messageMO = new MessageMO();
        messageMO.setFromUserId(userId);
        messageMO.setToUserId(vlogerId);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_MSG,
                                        "sys.msg."+MessageEnum.LIKE_VLOG.enValue,
                                                JsonUtils.objectToJson(messageMO));

    }
    @Override
    public Vlog getVlog(String id){
        return vlogMapper.selectByPrimaryKey(id);
    }

    @Transactional
    @Override
    public void userUnLikeVlog(String userId, String vlogId, String vlogerId) {
        MyLikedVlog likedVlog = new MyLikedVlog();
        likedVlog.setVlogId(vlogId);
        likedVlog.setUserId(userId);

        myLikedVlogMapper.delete(likedVlog);

        //?????????????????????????????????????????????????????????-1
        redis.decrement(REDIS_VLOGER_BE_LIKED_COUNTS+":"+vlogerId, 1);
        redis.decrement(REDIS_VLOG_BE_LIKED_COUNTS+":"+vlogId, 1);

        //????????????????????????????????????redis?????????????????????
        redis.del(REDIS_USER_LIKE_VLOG+":"+userId+":"+vlogId);
    }

    @Override
    public PagedGridResult getMyLikedVlogList(String userId, Integer page, Integer pageSize) {

        PageHelper.startPage(page, pageSize);
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        List<IndexVlogVO> list = vlogMapperCustom.getMyLikedVlogList(map);


        return setterPagedGrid(list, page);
    }

    @Override
    public PagedGridResult getMyFollowVlogList(String myId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);

        Map<String, Object> map = new HashMap<>();
        map.put("myId", myId);

        List<IndexVlogVO> list = vlogMapperCustom.getMyFollowVlogList(map);
        for (IndexVlogVO v : list) {
            String vlogerId = v.getVlogerId();
            String vlogId = v.getVlogId();

            if (StringUtils.isNotBlank(myId)){
                //???????????????????????????
                v.setDoIFollowVloger(true);

                //??????????????????????????????????????????
                v.setDoIFollowVloger(doILikeVlog(myId, vlogId));
            }

            //???????????????????????????????????????
            v.setLikeCounts(getVlogBeLikedCounts(vlogId));
        }

        return setterPagedGrid(list, page);
    }

    @Override
    public PagedGridResult getMyFriendVlogList(String myId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);

        Map<String, Object> map = new HashMap<>();
        map.put("myId", myId);

        List<IndexVlogVO> list = vlogMapperCustom.getMyFriendVlogList(map);
        for (IndexVlogVO v : list) {
            String vlogerId = v.getVlogerId();
            String vlogId = v.getVlogId();

            if (StringUtils.isNotBlank(myId)){
                //???????????????????????????
                v.setDoIFollowVloger(true);

                //??????????????????????????????????????????
                v.setDoIFollowVloger(doILikeVlog(myId, vlogId));
            }

            //???????????????????????????????????????
            v.setLikeCounts(getVlogBeLikedCounts(vlogId));
        }

        return setterPagedGrid(list, page);
    }

    @Transactional
    @Override
    public void flushCounts(String vlogId, Integer counts) {

        Vlog vlog = new Vlog();
        vlog.setId(vlogId);
        vlog.setLikeCounts(counts);
        vlogMapper.updateByPrimaryKeySelective(vlog);
    }
}
