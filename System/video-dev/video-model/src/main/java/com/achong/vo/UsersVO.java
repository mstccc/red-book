package com.achong.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UsersVO {
    @Id
    private String id;
    private String mobile;
    private String nickname;
    private String imoocNum;
    private String face;
    private Integer sex;
    private Date birthday;
    private String country;
    private String province;
    private String city;
    private String district;
    private String description;
    private String bgImg;
    private Integer canImoocNumBeUpdated;
    private Date createdTime;
    private Date updatedTime;

    private String userToken; //用户token，传递给前端

    private Integer myFollowsCounts;    //用户关注的博主的总数量
    private Integer myFansCounts;    //我的粉丝的总数
//    private Integer myLinkVlogCounts;    //所有喜欢我的vlog的总数
    private Integer totalLikeMeCounts;    //所有的喜欢我的总数
}