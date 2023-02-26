 package com.achong.service;

import com.achong.bo.VlogBo;
import com.achong.mo.MessageMO;
import com.achong.utils.PagedGridResult;
import com.achong.vo.IndexVlogVO;

import java.util.List;
import java.util.Map;


 public interface MsgService {

  /**
   *  创建消息
   * @param fromUserId 消息来自哪个用户
   * @param toUserId 消息发送给哪个用户
   * @param type  消息类型
   * @param msgContent 消息内容
   */
 public void createMsg(String fromUserId,
                       String toUserId,
                       Integer type,
                       Map msgContent);

  /**
   *  查询消息列表，具有分页功能
   * @param toUserId 消息发送到某对象的用户id
   * @param page
   * @param pageSize
   * @return
   */
 public List<MessageMO> queryList(String toUserId,
                                  Integer page,
                                  Integer pageSize);

}
