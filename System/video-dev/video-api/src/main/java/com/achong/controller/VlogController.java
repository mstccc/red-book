package com.achong.controller;

import com.achong.base.BaseInfoProperties;
import com.achong.bo.VlogBo;
import com.achong.enums.YesOrNo;
import com.achong.grace.result.GraceJSONResult;
import com.achong.service.VlogService;
import com.achong.utils.PagedGridResult;
import com.achong.vo.IndexVlogVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Api(tags = "VlogController 短视频相关业务功能的接口")
@RequestMapping("vlog")
@RestController
public class VlogController extends BaseInfoProperties {

    @Autowired
    private VlogService vlogService;

    @Value("${nacos.counts}")
    private Integer nacosCounts;

    //保存前端上传的视频数据
    @PostMapping("publish")
    public GraceJSONResult publish(@RequestBody VlogBo vlogBo){
        //FIXME 作业，校验VlogBo
        if (vlogBo == null){
            return GraceJSONResult.error();
        }

        vlogService.createVlog(vlogBo);
        return GraceJSONResult.ok();
    }


    @GetMapping("indexList")
    public GraceJSONResult indexList(@RequestParam(defaultValue = "") String userId,
                                     @RequestParam(defaultValue = "") String search,
                                     @RequestParam Integer page,
                                     @RequestParam Integer pageSize){
        if (page == null){
            page = COMMON_START_PAGE;
        }
        if (pageSize == null){
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult list = vlogService.getIndexVlogList(userId, search, page,  pageSize);
        return GraceJSONResult.ok(list);
    }

    /**
     * 视频详情页
     */
    @GetMapping("detail")
    public GraceJSONResult detail(@RequestParam(defaultValue = "") String userId,
                                     @RequestParam String vlogId){
        IndexVlogVO vlogVO = vlogService.getVlogDetailById(userId, vlogId);
        return GraceJSONResult.ok(vlogVO);
    }

    /**
     * 修改视频权限为私密
     */
    @PostMapping("changeToPrivate")
    public GraceJSONResult changeToPrivate(@RequestParam String userId,
                                            @RequestParam String vlogId){
        vlogService.changeToPrivateOrPublic(userId, vlogId, YesOrNo.YES.type);
        return GraceJSONResult.ok();
    }

    /**
     * 修改视频权限为公开
     */
    @PostMapping("changeToPublic")
    public GraceJSONResult changeToPublic(@RequestParam String userId,
                                           @RequestParam String vlogId){
        vlogService.changeToPrivateOrPublic(userId, vlogId, YesOrNo.NO.type);
        return GraceJSONResult.ok();
    }

    /**
     * 查询当前用户公开的视频
     */
    @GetMapping("myPublicList")
    public GraceJSONResult myPublicList(@RequestParam String userId,
                                        @RequestParam Integer page,
                                        @RequestParam Integer pageSize){
        if (page == null){
            page = COMMON_START_PAGE;
        }
        if (pageSize == null){
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult gridResult = vlogService.queryMyVlogList(userId,
                                                                page,
                                                                pageSize,
                                                                YesOrNo.NO.type);
        return GraceJSONResult.ok(gridResult);
    }

    /**
     * 查询当前用户的私密视频
     */
    @GetMapping("myPrivateList")
    public GraceJSONResult myPrivateList(@RequestParam String userId,
                                        @RequestParam Integer page,
                                        @RequestParam Integer pageSize){
        if (page == null){
            page = COMMON_START_PAGE;
        }
        if (pageSize == null){
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult gridResult = vlogService.queryMyVlogList(userId,
                page,
                pageSize,
                YesOrNo.YES.type);
        return GraceJSONResult.ok(gridResult);
    }

    /**
     * 查询赞过的视频
     */
    @GetMapping("myLikedList")
    public GraceJSONResult myLikedList(@RequestParam String userId,
                                         @RequestParam Integer page,
                                         @RequestParam Integer pageSize){
        if (page == null){
            page = COMMON_START_PAGE;
        }
        if (pageSize == null){
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult gridResult = vlogService.getMyLikedVlogList(userId,
                                                                    page,
                                                                    pageSize);
        return GraceJSONResult.ok(gridResult);
    }

    /**
     * 点赞视频
     */
    @PostMapping("like")
    public GraceJSONResult like(@RequestParam String userId,
                                @RequestParam String vlogerId,
                                @RequestParam String vlogId){
        //TODO 校验三个参数的准确性

        //我点赞的视频，关联关系保存到数据库
        vlogService.userLikeVlog(userId, vlogId, vlogerId);

        //点赞完毕，获得当前在redis中的总数
        // 比如获得总计数为 1k、1w、10w，假定阈值为2000
        // 此时1k满足2000，则触发入库
        String countStr = redis.get(REDIS_VLOG_BE_LIKED_COUNTS+":"+vlogId);
        System.out.println("=========="+REDIS_VLOG_BE_LIKED_COUNTS+":"+vlogId+", counts: "+countStr);
        Integer counts = 0;
        if (StringUtils.isNotBlank(countStr)){
            counts  = Integer.valueOf(countStr);
            if (counts >= nacosCounts){
                vlogService.flushCounts(vlogId, counts);
            }
        }

        return GraceJSONResult.ok();
    }

    /**
     * 取消点赞视频
     */
    @PostMapping("unlike")
    public GraceJSONResult unlike(@RequestParam String userId,
                                @RequestParam String vlogerId,
                                @RequestParam String vlogId){
        //TODO 校验三个参数的准确性

        //我取消点赞的视频，关联关系保存到数据库
        vlogService.userUnLikeVlog(userId, vlogId, vlogerId);

        return GraceJSONResult.ok();
    }

    /**
     * 获得用户点赞视频的总数
     */
    @PostMapping("totalLikedCounts")
    public GraceJSONResult totalLikedCounts(@RequestParam String vlogId){
        return GraceJSONResult.ok(vlogService.getVlogBeLikedCounts(vlogId));
    }

    /**
     * 查询赞过的视频
     */
    @GetMapping("followList")
    public GraceJSONResult followList(@RequestParam String myId,
                                       @RequestParam Integer page,
                                       @RequestParam Integer pageSize){
        if (page == null){
            page = COMMON_START_PAGE;
        }
        if (pageSize == null){
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult gridResult = vlogService.getMyFollowVlogList(myId,
                                                                    page,
                                                                    pageSize);
        return GraceJSONResult.ok(gridResult);
    }

    /**
     * 粉丝列表
     */
    @GetMapping("friendList")
    public GraceJSONResult friendList(@RequestParam String myId,
                                      @RequestParam Integer page,
                                      @RequestParam Integer pageSize){
        if (page == null){
            page = COMMON_START_PAGE;
        }
        if (pageSize == null){
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult gridResult = vlogService.getMyFriendVlogList(myId,
                                                                    page,
                                                                    pageSize);
        return GraceJSONResult.ok(gridResult);
    }

}
