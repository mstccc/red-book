package com.achong.mapper;

import com.achong.vo.CommentVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface CommentMapperCustom {

    /**
     *查询评论的列表
     */
    public List<CommentVO> getCommentList(@Param("paramMap")Map<String, Object> map);
}