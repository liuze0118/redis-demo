package com.lz.redis.demo.model.entity.mysql;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : liuze
 * @date: 2022/12/15 15:04
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "data_sence",autoResultMap = true)
public class DataSence {
    private int id;
    private String keyName;
}
