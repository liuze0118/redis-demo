package com.lz.redis.demo.service;

import com.lz.redis.demo.vo.User;

import java.util.List;

public interface UserService {
    User getUserById(int id);

    List<User> getUserByCondition(User user);

    int addUser(User user);

    int updateUser(User user) throws Exception;

    int deleteUserById(int id);

    int noTransactional(User user) throws Exception;
}
