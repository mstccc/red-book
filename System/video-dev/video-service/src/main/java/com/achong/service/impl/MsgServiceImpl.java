package com.achong.service.impl;

import com.achong.Repository.MessageRepository;
import com.achong.base.BaseInfoProperties;
import com.achong.enums.MessageEnum;
import com.achong.mo.MessageMO;
import com.achong.pojo.Users;
import com.achong.service.MsgService;
import com.achong.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MsgServiceImpl extends BaseInfoProperties implements MsgService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService;

    @Override
    public void createMsg(String fromUserId,
                          String toUserId,
                          Integer type,
                          Map msgContent) {
        Users fromUser = userService.getUsers(fromUserId);

        MessageMO messageMO = new MessageMO();

        messageMO.setFromUserId(fromUserId);
        messageMO.setFromNickName(fromUser.getNickname());
        messageMO.setFromFace(fromUser.getFace());

        messageMO.setToUserId(toUserId);

        messageMO.setMsgType(type);
        //消息可能为空
        if (msgContent != null){
            messageMO.setMsgContent(msgContent);
        }

        messageMO.setCreateTime(new Date());

        messageRepository.save(messageMO);
    }

    @Override
    public List<MessageMO> queryList(String toUserId, Integer page, Integer pageSize) {

        /**
         *  分页功能
         */
        Pageable pageable = PageRequest.of(page,
                                        pageSize,
                                        Sort.Direction.DESC,
                                        "createTime");

        List<MessageMO> list = messageRepository.
                findAllByToUserIdEqualsOrderByCreateTimeDesc(toUserId, pageable);
        System.out.println("查询用户"+toUserId+"的消息："+list.size());

        for (MessageMO msg : list) {
            //如果类型是关注消息，则需要查询我之前有没有关注他，用于在前端标记“互粉”“互关”
            if (msg.getMsgType() != null && msg.getMsgType() == MessageEnum.FOLLOW_YOU.type){   //关注
                Map map = msg.getMsgContent();
                if (map == null){
                    map = new HashMap();
                }

                //判断是否互粉
                String relationship = redis.get(REDIS_FANS_AND_VLOGER_RELATIONHiP + ":" + msg.getToUserId() + ":" + msg.getFromUserId());
                if (StringUtils.isNotBlank(relationship) && relationship.equalsIgnoreCase("1")){
                    map.put("isFriend", true);
                }else {
                    map.put("isFriend", false);
                }
                msg.setMsgContent(map);
            }
        }

        return list;
    }
}
