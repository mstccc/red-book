package com.achong.controller;

import com.achong.base.BaseInfoProperties;
import com.achong.bo.RegisLoginBO;
import com.achong.grace.result.GraceJSONResult;
import com.achong.grace.result.ResponseStatusEnum;
import com.achong.pojo.Users;
import com.achong.service.UserService;
import com.achong.utils.IPUtil;
import com.achong.utils.SMSUtils;
import com.achong.vo.UsersVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

/**
 * 短信通讯模块
 */
@RequestMapping("passport")
@RestController
@Slf4j
@Api(tags = "PassportController 通讯证接口模块")
public class PassportController extends BaseInfoProperties {

    @Autowired
    private SMSUtils smsUtils;

    @Autowired
    private UserService userService;

    @PostMapping("getSMSCode")
    public GraceJSONResult getSMSCode(@RequestParam String mobile,
                               HttpServletRequest request) throws Exception {
        //如果手机号为空，则直接返回，什么都不做
        if (StringUtils.isBlank(mobile)){
            return GraceJSONResult.ok();
        }

        // 获得用户ip
        String userIp = IPUtil.getRequestIp(request);
        // 根据用户ip进行限制，限制用户在60秒内只能获得一次验证码
        redis.setnx60s(MOBILE_SMSCODE+":"+userIp, userIp);

        //随机生成验证码
        String code = (int)((Math.random() * 9 + 1 ) * 10000 ) + "";
        //向接口发送短信请求
        smsUtils.sendSMS(mobile, code);
        log.info("为手机号："+mobile+", 生成的短信验证码："+code);

        // 把验证码放入到redis中，用于后续验证，有效时间30分钟
        redis.set(MOBILE_SMSCODE+":"+mobile, code,30*60);

        return GraceJSONResult.ok();
    }

    /**
     * 登录or注册
     *  注解@Valid用于实体类的校验,校验的异常信息被 GraceExceptionHandler 的 returnMethodArgumentNotValid 处理
     * @param regisLoginBo
//     * @param result
     * @param request
     * @return
     */
    @PostMapping("login")
    public GraceJSONResult login(@Valid @RequestBody RegisLoginBO regisLoginBo,
//                                      BindingResult result,
                                      HttpServletRequest request){
        String mobile = regisLoginBo.getMobile();
        String code = regisLoginBo.getSmsCode();

        //1. 从redis中获得验证码进行校验是否匹配
        String redisCode = redis.get(MOBILE_SMSCODE+":"+mobile);
        //是否为空串或者是否为null，因为验证码会过期
        if (StringUtils.isBlank(redisCode) || !redisCode.equalsIgnoreCase(code)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);  //提示：验证码过期或不匹配，请稍后再试！
        }
        //2. 查询数据库，判断用户是否存在
        Users user = userService.queryMobileIsExist(mobile);
        if (user == null){
            //如果用户为空则表示没有注册过，则需要注册信息入库
            user = userService.createUsers(mobile);
        }
        //3. 如果不为空，可以继续下方业务，保存用户会话信息，因为是前后端分离，所以用户会话信息存储在中间件
        String uToken = UUID.randomUUID().toString();
        redis.set(REDIS_USER_TOKEN+":"+user.getId(), uToken);

        //4. 用户登录注册成功以后删除redis中的短信验证码
        redis.del(MOBILE_SMSCODE+":"+mobile);

        //5. 返回用户信息，包含token令牌。
            //将查询出的用户复制一份，包含token令牌，将其返回到前端
        UsersVO usersVo = new UsersVO();
        BeanUtils.copyProperties(user, usersVo);    //借助BeanUtils工具拷贝对象
        usersVo.setUserToken(uToken);

        return GraceJSONResult.ok(usersVo);
    }


    //退出登录
    @PostMapping("logout")
    public GraceJSONResult logout(@RequestParam String userId){
        // 清除token信息和会话信息即可，前端也要清除，清除本地app中的用户信息和token
        redis.del(REDIS_USER_TOKEN + ":" + userId);
        log.info("token已清除，用户退出。");
        return GraceJSONResult.ok();
    }
}






