package com.achong.service;

import com.achong.bo.UpdatedUsersBO;
import com.achong.pojo.Users;
import com.achong.utils.PagedGridResult;
import com.achong.vo.VlogerVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FansService {
    /**
     * 关注
     * @param myId
     * @param vlogerId
     */
    public void doFollow (String myId, String vlogerId);

    /**
     * 取关
     * @param myId
     * @param vlogerId
     */
    public void doCancel(String myId, String vlogerId);

    /**
     * 查询用户是否关注博主
     * @param myId
     * @param VlogerId
     * @return
     */
    public boolean queryDoIFollowVloger(String myId, String VlogerId);

    /**
     * 查询我关注的博主列表
     * @param myId
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult queryMyFollows(String myId,
                                           Integer page,
                                           Integer pageSize);

    /**
     * 查询我的粉丝列表
     * @param myId
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult queryMyFans(String myId,
                                          Integer page,
                                          Integer pageSize);

}
