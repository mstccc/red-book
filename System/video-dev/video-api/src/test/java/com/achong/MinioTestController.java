package com.achong;

import com.achong.grace.result.GraceJSONResult;
import com.achong.model.Stu;
import com.achong.utils.SMSUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@Api(tags = "Hello 测试的接口")
public class MinioTestController {

    @Autowired
    private SMSUtils smsUtils;

    @GetMapping("sms")
    public Object sms() throws Exception {
        String code = "123456";
        smsUtils.sendSMS("17325919473", code);
        return GraceJSONResult.ok();
    }

    @ApiOperation(value = "hello - 这是一个hello的测试路由")
    @GetMapping("/hello")
    public Object hello(){
        Stu stu = new Stu("AChong", 18);
        log.info(stu.toString());
        log.debug(stu.toString());
        log.warn(stu.toString());
        log.error(stu.toString());
        return GraceJSONResult.ok(stu.toString());
    }

}
