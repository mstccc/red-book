package com.achong.service;

import com.achong.bo.UpdatedUsersBO;
import com.achong.pojo.Users;

public interface UserService {
    /**
     * 判断用户是否存在，如果存在则返回用户信息
     * @param mobile    用户手机号
     * @return
     */
    public Users queryMobileIsExist(String mobile);

    /**
     * 创建用户信息，并返回用户对象
     * @param mobile
     * @return
     */
    public Users createUsers(String mobile);

    /**
     * 根据用户主键查询用户信息
     * @param userId
     * @return
     */
    public Users getUsers(String userId);


    /**
     * 用户信息修改，并返回更新后的用户信息
     * @param updatedUsersBo
     * @return
     */
    public Users updatedUserInfo (UpdatedUsersBO updatedUsersBo);

    /**
     * 用户信息修改，并返回更新后的用户信息
     * @param updatedUsersBo
     * @return
     */
    public Users updatedUserInfo (UpdatedUsersBO updatedUsersBo, Integer type);

}
