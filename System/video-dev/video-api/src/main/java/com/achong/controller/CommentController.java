package com.achong.controller;

import com.achong.base.BaseInfoProperties;
import com.achong.bo.CommentBO;
import com.achong.enums.MessageEnum;
import com.achong.grace.result.GraceJSONResult;
import com.achong.model.Stu;
import com.achong.pojo.Comment;
import com.achong.pojo.Vlog;
import com.achong.service.CommentService;
import com.achong.service.MsgService;
import com.achong.service.VlogService;
import com.achong.utils.SMSUtils;
import com.achong.vo.CommentVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Api(tags = "CommentController 评论模块的接口")
@RequestMapping("comment")
@RestController
public class CommentController extends BaseInfoProperties {

    @Autowired
    private CommentService commentService;

    @Autowired
    private MsgService msgService;

    @Autowired
    private VlogService vlogService;

    /**
     * 保存评论
     */
    @PostMapping("create")
    public GraceJSONResult create(@RequestBody @Valid CommentBO commentBO){

        CommentVO commentVO = commentService.createComment(commentBO);

        return GraceJSONResult.ok(commentVO);
    }

    /**
     * 评论总数查询
     */
    @GetMapping("counts")
    public GraceJSONResult counts(@RequestParam String vlogId){

        String countsStr = redis.get(REDIS_VLOG_COMMENT_COUNTS+":"+vlogId);
        if (StringUtils.isBlank(countsStr)){
            countsStr = "0";
        }

        return GraceJSONResult.ok(Integer.valueOf(countsStr));
    }

    /**
     * 评论查询
     */
    @GetMapping("list")
    public GraceJSONResult list(@RequestParam String vlogId,
                                @RequestParam(defaultValue = "") String userId,
                                @RequestParam Integer page,
                                @RequestParam Integer pageSize){

        return GraceJSONResult.ok(commentService.queryVlogComments(vlogId, userId, page, pageSize));
    }

    /**
     * 删除评论
     */
    @DeleteMapping("delete")
    public GraceJSONResult delete(@RequestParam String commentUserId,
                                @RequestParam String commentId,
                                @RequestParam String vlogId){
        commentService.deleteComment(commentUserId, commentId, vlogId);
        return GraceJSONResult.ok();
    }

    /**
     * 评论点赞
     */
    @PostMapping("like")
    public GraceJSONResult like(@RequestParam String commentId,
                                  @RequestParam String userId){
        // DUTO 有可能BigKey
        redis.incrementHash(REDIS_VLOG_COMMENT_LIKED_COUNTS, commentId, 1);
        redis.setHashValue(REDIS_USER_LIKE_COMMENT, userId + ":" + commentId, "1");

        // 系统消息：评论/回复
        Comment comment = commentService.getComment(commentId);
        Vlog vlog = vlogService.getVlog(comment.getVlogId());
        Map msgContent = new HashMap();
        msgContent.put("vlogId", vlog.getId());
        msgContent.put("vlogCover", vlog.getCover());
        msgContent.put("commentId", commentId);
        msgContent.put("commentContent", comment.getContent());

        msgService.createMsg(userId,
                comment.getCommentUserId(),
                MessageEnum.LIKE_COMMENT.type,
                msgContent);

        return GraceJSONResult.ok();
    }
    /**
     * 取消评论点赞
     */
    @PostMapping("unlike")
    public GraceJSONResult unLike(@RequestParam String commentId,
                                @RequestParam String userId){
        redis.decrementHash(REDIS_VLOG_COMMENT_LIKED_COUNTS, commentId, 1);
        redis.hdel(REDIS_USER_LIKE_COMMENT, userId + ":" + commentId, "1");
        return GraceJSONResult.ok();
    }
    /**
     *  查询点赞
     */
}
