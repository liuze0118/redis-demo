package com.lz.redis.demo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lz.redis.demo.config.EncTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName(value = "user",autoResultMap = true)
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @TableField(value = "id")
    private int id;
    @TableField(value = "name")
    private String name;
    @TableField(value = "nick_name")
    private String nickName;
    @TableField(value = "password")
    private String password;
    @TableField(value = "phone_number",typeHandler = EncTypeHandler.class)
    private String phoneNumber;
}
