package com.lz.redis.demo.dao;

import com.lz.redis.demo.vo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDao {
    User getUserById(@Param("id") int id);

    List<User> selectUserByCondition(User user);

    int addUser(User user);

    int updateUser(User user);

    int deleteUserById(int id);
}
