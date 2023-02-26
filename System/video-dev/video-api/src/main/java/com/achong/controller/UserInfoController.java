package com.achong.controller;

import com.achong.base.BaseInfoProperties;
import com.achong.bo.UpdatedUsersBO;
import com.achong.config.MinIOConfig;
import com.achong.enums.FileTypeEnum;
import com.achong.enums.UserInfoModifyType;
import com.achong.grace.result.GraceJSONResult;
import com.achong.grace.result.ResponseStatusEnum;
import com.achong.pojo.Users;
import com.achong.service.UserService;
import com.achong.utils.MinIOUtils;
import com.achong.vo.UsersVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Api(tags = "UserInfoController 用户信息接口模块")
@RestController
@RequestMapping("userInfo")
public class UserInfoController extends BaseInfoProperties {

    @Autowired
    private UserService userService;
    @Autowired
    private MinIOConfig minIOConfig;

    @GetMapping("query")
    public Object query(@RequestParam String userId){
        Users user = userService.getUsers(userId);

        UsersVO usersVo = new UsersVO();
        BeanUtils.copyProperties(user, usersVo);

        //我的关注博主总数量
        String myFollowsCountsStr = redis.get(REDIS_MY_FOLLOWS_COUNTS+":"+userId);
        //我的粉丝总数
        String myFansCountsStr = redis.get(REDIS_MY_FANS_COUNTS+":"+userId);
        //用户获赞总数，视频+评论（点赞、喜欢）总和
        String LikedVlogCountsStr = redis.get(REDIS_VLOG_BE_LIKED_COUNTS+":"+userId);
        String likedVlogerCountsStr = redis.get(REDIS_VLOGER_BE_LIKED_COUNTS+":"+userId);

        Integer myFollowsCounts = 0;
        Integer myFansCounts = 0;
        Integer likeVlogCounts = 0;
        Integer likeVlogerCounts = 0;
        Integer totalLikeMeCounts = 0;

        if (StringUtils.isNotBlank(myFollowsCountsStr)){
            myFollowsCounts = Integer.valueOf(myFollowsCountsStr);
        }
        if (StringUtils.isNotBlank(myFansCountsStr)){
            myFansCounts = Integer.valueOf(myFansCountsStr);
        }
        if (StringUtils.isNotBlank(LikedVlogCountsStr)){
            likeVlogCounts = Integer.valueOf(LikedVlogCountsStr);
        }
        if (StringUtils.isNotBlank(likedVlogerCountsStr)){
            likeVlogerCounts = Integer.valueOf(likedVlogerCountsStr);
        }
        totalLikeMeCounts = likeVlogCounts + likeVlogerCounts;

        usersVo.setMyFollowsCounts(myFollowsCounts);
        usersVo.setMyFansCounts(myFansCounts);
        usersVo.setTotalLikeMeCounts(totalLikeMeCounts);

        return GraceJSONResult.ok(usersVo);
    }

    @PostMapping("modifyUserInfo")
    public GraceJSONResult modifyUserInfo(@RequestBody UpdatedUsersBO updatedUsersBO,
                                          @RequestParam Integer type)
                                            throws Exception{
        //字段校验
        UserInfoModifyType.checkUserInfoTypeIsRight(type) ;

        Users newUserInfo = userService.updatedUserInfo(updatedUsersBO, type);
        return GraceJSONResult.ok(newUserInfo);
    }

    /**
     * 头像和背景的更新
     * @param userId    用户id
     * @param type  修改类型，1是背景，2是头像
     * @param file  上传的图片
     * @return  返回更新后的用户信息
     * @throws Exception
     */
    @PostMapping("modifyImage")
    public GraceJSONResult modifyImage (@RequestParam String userId,
                                        @RequestParam Integer type,
                                        MultipartFile file) throws Exception {
        //判断传入参数类型是否是1或2
        if (type != FileTypeEnum.BGIMG.type && type != FileTypeEnum.FACE.type){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);   //文件上传失败！
        }

        //图片上传到服务器
        String fileName = file.getOriginalFilename();
        MinIOUtils.uploadFile(minIOConfig.getBucketName(),
                fileName,
                file.getInputStream());

        //得到图片地址
        String imgUrl = minIOConfig.getFileHost()
                + "/"
                + minIOConfig.getBucketName()
                + "/"
                + fileName;

        //更新图片地址到数据库
        UpdatedUsersBO updatedUsersBO = new UpdatedUsersBO();
        updatedUsersBO.setId(userId);
        if (type == FileTypeEnum.BGIMG.type){
            updatedUsersBO.setBgImg(imgUrl);
        } else{
            updatedUsersBO.setFace(imgUrl);
        }
        Users users = userService.updatedUserInfo(updatedUsersBO);
        return GraceJSONResult.ok(users);
    }


}
