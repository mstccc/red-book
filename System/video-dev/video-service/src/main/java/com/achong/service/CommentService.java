package com.achong.service;

import com.achong.bo.CommentBO;
import com.achong.pojo.Comment;
import com.achong.utils.PagedGridResult;
import com.achong.vo.CommentVO;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentService {
    /**
     * 发表评论
     */
    public CommentVO createComment (CommentBO commentBO);

    /**
     * 查询评论的列表
     */
    public PagedGridResult queryVlogComments (String vlogId,
                                              String userId,
                                              Integer page,
                                              Integer pageSize);

    /**
     * 删除评论
     */
    public void deleteComment(String commentUserId,
                              String commentId,
                              String vlogId);


    /**
     * 根据主键查询comment
     */
    public Comment getComment(String id);

}
