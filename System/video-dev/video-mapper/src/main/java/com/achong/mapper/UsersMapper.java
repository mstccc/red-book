package com.achong.mapper;

import com.achong.my.mapper.MyMapper;
import com.achong.pojo.Users;
import org.springframework.stereotype.Repository;

@Repository //数据层的注解，可加可不加
public interface UsersMapper extends MyMapper<Users> {
}