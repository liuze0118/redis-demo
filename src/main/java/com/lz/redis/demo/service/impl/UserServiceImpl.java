package com.lz.redis.demo.service.impl;

import com.lz.redis.demo.dao.UserDao;
import com.lz.redis.demo.service.RedisService;
import com.lz.redis.demo.service.UserService;
import com.lz.redis.demo.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private RedisService redisService;

    @Autowired
    private UserDao userDao;
    @Override
    public User getUserById(int id) {
        return userDao.getUserById(id);
    }

    @Override
    public List<User> getUserByCondition(User user) {
        return userDao.selectUserByCondition(user);
    }

    @Override
    public int addUser(User user) {
        return userDao.addUser(user);
    }

    @Override
    @Transactional
    public int updateUser(User user) throws Exception {

        int i = userDao.updateUser(user);
        if(user.getId() == 3){
            throw new RuntimeException("测试回滚");
        }
        return i;
    }

    @Override
    public int deleteUserById(int id) {
        return userDao.deleteUserById(id);
    }

    @Override
    public int noTransactional(User user) throws Exception {
        return updateUser(user);
    }
}
