package com.achong.mapper;

import com.achong.my.mapper.MyMapper;
import com.achong.pojo.Comment;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentMapper extends MyMapper<Comment> {


    /**
     * 删除评论
     */
    public void deleteComment(String commentUserId,
                              String commentId,
                              String vlogId);
}