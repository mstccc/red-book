package com.achong.controller;

import com.achong.base.BaseInfoProperties;
import com.achong.bo.VlogBo;
import com.achong.enums.YesOrNo;
import com.achong.grace.result.GraceJSONResult;
import com.achong.grace.result.ResponseStatusEnum;
import com.achong.pojo.Users;
import com.achong.service.FansService;
import com.achong.service.UserService;
import com.achong.service.VlogService;
import com.achong.utils.PagedGridResult;
import com.achong.vo.FansVo;
import com.achong.vo.IndexVlogVO;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Api(tags = "FansController 粉丝相关业务功能的接口")
@RequestMapping("fans")
@RestController
public class FansController extends BaseInfoProperties {

    @Autowired
    private FansService fansService;

    @Autowired
    private UserService userService;

    /***
     *  关注
     */
    @PostMapping("follow")
    public GraceJSONResult follow(@RequestParam String myId,
                                   @RequestParam String vlogerId){
        System.out.println("follow: 关注=============================");

        //判断两个id不能为空
        if (StringUtils.isBlank(myId) || StringUtils.isBlank(vlogerId)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);    //系统繁忙，请稍后再试！
        }
        //判断当前用户，自己不能关注自己
        if (myId.equalsIgnoreCase(vlogerId)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_RESPONSE_NO_INFO);
        }
        //判断两个id对应的用户是否存在
        Users vloger = userService.getUsers(vlogerId);
        Users myInfo = userService.getUsers(myId);
        //fixme：两个用户id的数据库查询后的判断，是分开好还是合并好？
        if (myInfo == null || vloger == null){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_RESPONSE_NO_INFO);
        }

        //保存粉丝关系到数据库
        fansService.doFollow(myId, vlogerId);

        //博主的粉丝+1，我的关注+1
        redis.increment(REDIS_MY_FOLLOWS_COUNTS + ":" + myId, 1);
        redis.increment(REDIS_MY_FANS_COUNTS + ":" + vlogerId, 1);

        //我和博主的关联关系，依赖redis，不要存储数据库，避免db的性能瓶颈。
        redis.set(REDIS_FANS_AND_VLOGER_RELATIONHiP + ":" + myId + ":" + vlogerId, "1");

        return GraceJSONResult.ok();
    }

    /**
     * 取消关注
     */
    @PostMapping("cancel")
    public GraceJSONResult cancel(@RequestParam String myId,
                                  @RequestParam String vlogerId){
        System.out.println("cancel: 取消关注====================================");
        //删除业务的执行
        fansService.doCancel(myId, vlogerId);

        //博主的粉丝-1，我的关注-1
        redis.decrement(REDIS_MY_FOLLOWS_COUNTS + ":" + myId, 1);
        redis.decrement(REDIS_MY_FANS_COUNTS + ":" + vlogerId, 1);

        // 解除关联关系
        redis.del(REDIS_FANS_AND_VLOGER_RELATIONHiP + ":" + myId + ":" + vlogerId);

        return GraceJSONResult.ok();
    }

    /**
     * 查询我是否关注此up主
     */
    @GetMapping("queryDoIFollowVloger")
    public GraceJSONResult queryDoIFollowVloger(@RequestParam String myId,
                                  @RequestParam String vlogerId){
        return GraceJSONResult.ok(fansService.queryDoIFollowVloger(myId, vlogerId));
    }

    /**
     * 查询我的关注
     */
    @GetMapping("queryMyFollows")
    public GraceJSONResult queryMyFollows(@RequestParam String myId,
                                          @RequestParam Integer page,
                                          @RequestParam Integer pageSize){
        System.out.println("queryMyFollows: 查询我的关注===================");
        return GraceJSONResult.ok(fansService.queryMyFollows(myId, page, pageSize));
    }

    /**
     * 查询我的粉丝
     */
    @GetMapping("queryMyFans")
    public GraceJSONResult queryMyFans(@RequestParam String myId,
                                          @RequestParam Integer page,
                                          @RequestParam Integer pageSize){
        System.out.println("queryMyFans====================");
        PagedGridResult gridResult = fansService.queryMyFans(myId, page, pageSize);
        return GraceJSONResult.ok(gridResult);
    }
}
