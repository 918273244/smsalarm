package com.xsxx.mapper;

import com.xsxx.pojo.User;


public interface UserMapper extends BaseMapper<User, Integer>{

    public User findByUserName(String name);
}
