package com.achong.controller;

import com.achong.base.RabbitMQConfig;
import com.achong.grace.result.GraceJSONResult;
import com.achong.model.Stu;
import com.achong.utils.SMSUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPSClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@Slf4j
@Api(tags = "Hello 测试的接口")
@RefreshScope
public class HelloController {

    @Value("${nacos.counts}")
    private Integer nacosCounts;

    @Autowired
    private SMSUtils smsUtils;

    @GetMapping("sms")
    public Object sms() throws Exception {
        String code = "123456";
        smsUtils.sendSMS("17325919473", code);
        return GraceJSONResult.ok();
    }

    @ApiOperation(value = "nacosCounts - 配置测试路由")
    @GetMapping("/nacosCounts")
    public GraceJSONResult nacosCounts(){

        return GraceJSONResult.ok("nacosCounts的数值为："+nacosCounts);
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

    @Autowired
    public RabbitTemplate rabbitTemplate;

    @GetMapping("produce")
    public Object produce(){

        rabbitTemplate.convertAndSend(
                            RabbitMQConfig.EXCHANGE_MSG,
                            "sys.msg.send",
                            "我发送了一个消息");

        return GraceJSONResult.ok();
    }

    // FTP
    @RequestMapping
    public String connFTP() throws IOException {
        FTPClient client = new FTPSClient();
        client.connect("192.168.1.5", 21);
        client.login("ftptest", "ftptest");
        int replyCode = client.getReplyCode();

        return String.valueOf(replyCode);
    }

}
