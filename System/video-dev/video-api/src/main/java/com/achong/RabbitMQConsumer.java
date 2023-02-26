package com.achong;

import com.achong.base.RabbitMQConfig;
import com.achong.enums.MessageEnum;
import com.achong.exceptions.GraceException;
import com.achong.grace.result.ResponseStatusEnum;
import com.achong.mo.MessageMO;
import com.achong.service.MsgService;
import com.achong.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitMQConsumer {

    @Autowired
    private MsgService msgService;

    /**
     *  监听队列消息，分别是 关注、点赞视频、评论视频、回复评论、点赞评论。
     *  如果监听到，则创建对应类型的MongoDB消息
     */
    @RabbitListener(queues = {RabbitMQConfig.QUEUE_SYS_MSG})    //监听的队列
    public void watchQueue(String payload, Message message){
        log.info(payload);

        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        log.info(routingKey);

        MessageMO messageMO = JsonUtils.jsonToPojo(payload, MessageMO.class);

        // String.equalsIgnoreCase，于equals相似，该方法无视大小写
        if (routingKey.equalsIgnoreCase("sys.msg."+ MessageEnum.FOLLOW_YOU.enValue)){   // 关注
            msgService.createMsg(messageMO.getFromUserId(),
                                messageMO.getToUserId(),
                                MessageEnum.FOLLOW_YOU.type,
                                null);
        }else if (routingKey.equalsIgnoreCase("sys.msg."+MessageEnum.LIKE_VLOG.enValue)){   // 点赞视频
            msgService.createMsg(messageMO.getFromUserId(),
                                messageMO.getToUserId(),
                                MessageEnum.LIKE_VLOG.type,
                                null);
        }else if (routingKey.equalsIgnoreCase("sys.msg."+MessageEnum.COMMENT_VLOG.enValue)){    // 评论视频
            msgService.createMsg(messageMO.getFromUserId(),
                                 messageMO.getToUserId(),
                                 MessageEnum.COMMENT_VLOG.type,
                                null);
        }else if (routingKey.equalsIgnoreCase("sys.msg."+MessageEnum.REPLY_YOU.enValue)){   // 回复评论
            msgService.createMsg(messageMO.getFromUserId(),
                                messageMO.getToUserId(),
                                MessageEnum.REPLY_YOU.type,
                                null);
        }else if (routingKey.equalsIgnoreCase("sys.msg."+MessageEnum.LIKE_COMMENT.enValue)){    // 点赞评论
            msgService.createMsg(messageMO.getFromUserId(),
                                messageMO.getToUserId(),
                                MessageEnum.LIKE_COMMENT.type,
                                null);
        }else {
            GraceException.display(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        }
    }


}
