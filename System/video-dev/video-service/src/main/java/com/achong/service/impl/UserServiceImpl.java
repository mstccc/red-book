package com.achong.service.impl;

import com.achong.bo.UpdatedUsersBO;
import com.achong.enums.Sex;
import com.achong.enums.UserInfoModifyType;
import com.achong.enums.YesOrNo;
import com.achong.exceptions.GraceException;
import com.achong.grace.result.ResponseStatusEnum;
import com.achong.mapper.UsersMapper;
import com.achong.pojo.Users;
import com.achong.service.UserService;
import com.achong.utils.DateUtil;
import com.achong.utils.DesensitizationUtil;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private Sid sid;

    //默认的用户头像地址
    private static final String USER_FACE1 = "https://thirdqq.qlogo.cn/g?b=qq&nk=2107739&s=100";

    /**
     * 查询手机号是否存在
     * @param mobile    用户手机号
     * @return
     */
    @Override
    public Users queryMobileIsExist(String mobile) {
        //创建一个被查询的实例
        Example userExample = new Example(Users.class);
        //创建查询条件
        Example.Criteria criteria = userExample.createCriteria();
        //判断是否相等，将数据库的mobile字段和传入的mobile对比。
        criteria.andEqualTo("mobile", mobile);
        //开始查询并返回一个key
        Users user = usersMapper.selectOneByExample(userExample);
        return user;
    }

    @Transactional  //新增操作涉及到事务，该注解开启事务
    @Override
    public Users createUsers(String mobile) {

        //获得全局唯一主键
        String userId = sid.nextShort();

        Users user = new Users();
        user.setId(userId);
        user.setMobile(mobile);
        user.setNickname("用户："+ DesensitizationUtil.commonDisplay(mobile));
        user.setImoocNum("用户："+ DesensitizationUtil.commonDisplay(mobile));
        user.setFace(USER_FACE1);

        user.setBirthday(DateUtil.stringToDate("1900-01-01"));
        user.setSex(Sex.secret.type);

        user.setCountry("中国");
        user.setProvince("");
        user.setCity("");
        user.setDistrict("");
        user.setDescription("");
        user.setCanImoocNumBeUpdated(YesOrNo.YES.type);

        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());

        usersMapper.insert(user);
        return user;
    }

    /**
     * 根据用户主键查询用户信息
     * @param userId
     * @return
     */
    @Override
    public Users getUsers(String userId) {
        return usersMapper.selectByPrimaryKey(userId);
    }

    /**
     * 用户信息修改
     * @param updatedUsersBo
     * @return
     */
    @Transactional
    @Override
    public Users updatedUserInfo(UpdatedUsersBO updatedUsersBo) {

        Users pendingUser = new Users();
        //将updatedUsersBo复制到pendingUser
        BeanUtils.copyProperties(updatedUsersBo, pendingUser);
        //根据主键进行选择更新
        int result = usersMapper.updateByPrimaryKeySelective(pendingUser);
        if (result != 1){
            GraceException.display(ResponseStatusEnum.USER_UPDATE_ERROR);   //用户信息更新失败，请联系管理员！
        }
        //返回更新后的用户
        return getUsers(updatedUsersBo.getId());
    }

    /**
     * 用户信息更新的前置判断，判断用户昵称和慕课号是否唯一，账号修改一次后不能再修改
     */
    @Transactional
    @Override
    public Users updatedUserInfo(UpdatedUsersBO updatedUsersBo, Integer type) {

        //创建一个被查询的实例
        Example example = new Example(Users.class);
        //创建查询条件
        Example.Criteria criteria = example.createCriteria();
        //通过枚举判断用户昵称
        if (type == UserInfoModifyType.NICKNAME.type){
            //将前台传过来的字段与数据库里的字段对比
            criteria.andEqualTo("nickname", updatedUsersBo.getNickname());
            //执行查询并返回用户
            Users user = usersMapper.selectOneByExample(example);
            if (user != null){
                GraceException.display(ResponseStatusEnum.USER_INFO_UPDATED_NICKNAME_EXIST_ERROR);  //昵称已经存在
            }
        }
        //通过枚举判断慕课号
        if (type == UserInfoModifyType.IMOOCNUM.type){
            //将前台传过来的字段与数据库里的字段对比
            criteria.andEqualTo("imoocNum", updatedUsersBo.getImoocNum());
            Users user = usersMapper.selectOneByExample(example);
            if (user != null){
                GraceException.display(ResponseStatusEnum.USER_INFO_UPDATED_NICKNAME_EXIST_ERROR);  //昵称已经存在
            }
            Users tempUser = getUsers(updatedUsersBo.getId());
            if (tempUser.getCanImoocNumBeUpdated() == YesOrNo.NO.type){
                GraceException.display(ResponseStatusEnum.USER_INFO_CANT_UPDATED_IMOOCNUM_ERROR);
            }
            //修改canImoocNumBeUpdated标记，
            updatedUsersBo.setCanImoocNumBeUpdated(YesOrNo.NO.type);
        }
        //返回
        return updatedUserInfo(updatedUsersBo);
    }
}
