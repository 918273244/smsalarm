package com.xsxx.service;

import com.xsxx.pojo.User;

import java.util.List;

public interface UserService {
    String name = "userService";

    public User findByUserName(String username);

    public List<User> findAll();
}
