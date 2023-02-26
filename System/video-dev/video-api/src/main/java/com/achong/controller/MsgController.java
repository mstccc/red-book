package com.achong.controller;

import com.achong.base.BaseInfoProperties;
import com.achong.grace.result.GraceJSONResult;
import com.achong.mo.MessageMO;
import com.achong.model.Stu;
import com.achong.service.MsgService;
import com.achong.utils.SMSUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@Api(tags = "MsgController 消息功能模块的接口")
@RestController
@RequestMapping("msg")
public class MsgController extends BaseInfoProperties {

    @Autowired
    private MsgService msgService;

    /**
     * 系统消息查询
     */
    @GetMapping("list")
    public GraceJSONResult list(@RequestParam String userId,
                                @RequestParam Integer page,
                                @RequestParam Integer pageSize){

        //mongoDB从0开始分页， 区别与数据库
        if (page == null){
            page = COMMON_START_PAGE_ZERO;
        }
        if (pageSize == null){
            pageSize = COMMON_PAGE_SIZE;
        }

        List<MessageMO> list = msgService.queryList(userId, page, pageSize);
        for (MessageMO msg : list) {
            System.out.println(msg);
        }

        return GraceJSONResult.ok(list);
    }


}
