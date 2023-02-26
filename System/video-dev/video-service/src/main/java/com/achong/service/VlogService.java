 package com.achong.service;

import com.achong.bo.UpdatedUsersBO;
import com.achong.bo.VlogBo;
import com.achong.pojo.Users;
import com.achong.pojo.Vlog;
import com.achong.utils.PagedGridResult;
import com.achong.vo.IndexVlogVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

 public interface VlogService {

  /**
   * 根据主键查询Vlog
   */
   public Vlog getVlog(String id);

    /**
     * 新增vlog视频
     */
    public void createVlog(VlogBo vlogBo);


     /**
      * 查询首页、搜索页的vlog列表
      * @param search   查询条件，视频标题，可以为空
      */
     public PagedGridResult getIndexVlogList(String userId,
                                             String search,
                                             Integer page,
                                             Integer pageSize);

     /**
      * 根据视频主键查询vlog
      */
     public IndexVlogVO getVlogDetailById(String userId, String vlogId);

     /**
      * 修改用户视频的权限为公开/私密
      */
     public void changeToPrivateOrPublic(String userId, String vlogId, Integer yesOrNo);

     /**
      * 查询用户的公开/私密的视频列表
      */
     public PagedGridResult queryMyVlogList(String userId,
                                            Integer page,
                                            Integer pageSize,
                                            Integer yesOrNo);

  //喜欢视频
  public default void userLikeVlog(String userId, String vlogId, String vlogerId) {

  }

  //不喜欢视频
  public void userUnLikeVlog(String userId,String vlogId, String vlogerId);

  /**
   * 获得用户点赞视频的总数
   */
  public Integer getVlogBeLikedCounts(String vlogId);

  /**
   * 查询用户点赞过的短视频
   */
  public PagedGridResult getMyLikedVlogList(String userId,
                                         Integer page,
                                         Integer pageSize);

  /**
   * 查询用户关注的博主发布的短视频列表
   */
  public PagedGridResult getMyFollowVlogList(String myId,
                                            Integer page,
                                            Integer pageSize);

  /**
   * 查询朋友发布的短视频列表
   */
  public PagedGridResult getMyFriendVlogList(String myId,
                                             Integer page,
                                             Integer pageSize);
  /**
   * 把counts输入数据库
   */
 public void flushCounts(String vlogId, Integer counts);
}
