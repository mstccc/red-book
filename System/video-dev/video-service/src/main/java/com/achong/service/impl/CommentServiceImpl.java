package com.achong.service.impl;

import com.achong.base.BaseInfoProperties;
import com.achong.bo.CommentBO;
import com.achong.enums.MessageEnum;
import com.achong.enums.YesOrNo;
import com.achong.mapper.CommentMapper;
import com.achong.mapper.CommentMapperCustom;
import com.achong.pojo.Comment;
import com.achong.pojo.Vlog;
import com.achong.service.CommentService;
import com.achong.service.MsgService;
import com.achong.service.VlogService;
import com.achong.utils.PagedGridResult;
import com.achong.vo.CommentVO;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl extends BaseInfoProperties implements CommentService{

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CommentMapperCustom commentMapperCustom;

    @Autowired
    private Sid sid;

    @Autowired
    private MsgService msgService;

    @Autowired
    private VlogService vlogService;

    /**
     * 发表评论
     */
    public CommentVO createComment (CommentBO commentBO){
        String commentId = sid.nextShort();

        Comment comment = new Comment();
        comment.setId(commentId);

        comment.setVlogId(commentBO.getVlogId());
        comment.setVlogerId(commentBO.getVlogerId());

        comment.setCommentUserId(commentBO.getCommentUserId());
        comment.setFatherCommentId(commentBO.getFatherCommentId());
        comment.setContent(commentBO.getContent());

        comment.setLikeCounts(0);
        comment.setCreateTime(new Date());

        commentMapper.insert(comment);

        //redis操作放在service中，评论总数的累加
        redis.increment(REDIS_VLOG_COMMENT_COUNTS + ":" + commentBO.getVlogId(),1);

        //留言后的最新评论需要返回给前端进行展示
        CommentVO commentVO = new CommentVO();
        BeanUtils.copyProperties(comment, commentVO);

        // 系统消息：评论/回复
        Vlog vlog = vlogService.getVlog(commentBO.getVlogId());
        Map msgContent = new HashMap();
        msgContent.put("vlogId", vlog.getId());
        msgContent.put("vlogCover", vlog.getCover());
        msgContent.put("commentId", commentId);
        msgContent.put("commentContent", commentBO.getContent());
        Integer type = MessageEnum.COMMENT_VLOG.type;
        if (StringUtils.isNotBlank(commentBO.getFatherCommentId()) &&
                !commentBO.getFatherCommentId().equalsIgnoreCase("0")){
            type = MessageEnum.REPLY_YOU.type;
        }
        msgService.createMsg(commentBO.getCommentUserId(),
                            commentBO.getVlogerId(),
                            type,
                            msgContent);

        return commentVO;
    }

    /**
     * 查询评论的列表
     */
    @Override
    public PagedGridResult queryVlogComments(String vlogId, String userId,Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("vlogId", vlogId);

        PageHelper.startPage(page, pageSize);

        List<CommentVO> list = commentMapperCustom.getCommentList(map);
        for (CommentVO cv : list) {
            String commentId = cv.getCommentId();

            //当前短视频的某个评论的点赞总数
            String countsStr = redis.getHashValue(REDIS_VLOG_COMMENT_LIKED_COUNTS, commentId);
            Integer counts = 0;
            if (StringUtils.isNotBlank(countsStr)){
                counts = Integer.valueOf(countsStr);
            }
            cv.setLikeCounts(counts);

            //判断当前用户是否点赞过该评论
            String doILike = redis.hget(REDIS_USER_LIKE_COMMENT, userId + ":" + commentId);
            if (StringUtils.isNotBlank(doILike) && doILike.equalsIgnoreCase("1")){
                cv.setIsLike(YesOrNo.YES.type);
            }
        }
        return setterPagedGrid(list, page);
    }

    /**
     * 删除评论
     */
    @Override
    public void deleteComment(String commentUserId, String commentId, String vlogId) {
        Comment pendingDelete = new Comment();
        pendingDelete.setId(commentId);
        pendingDelete.setCommentUserId(commentUserId);

        commentMapper.delete(pendingDelete);

        //评论总数的累减
        redis.decrement(REDIS_VLOG_COMMENT_COUNTS + ":" + vlogId,1);
    }

    @Override
    public Comment getComment(String id) {
        return commentMapper.selectByPrimaryKey(id);
    }
}
