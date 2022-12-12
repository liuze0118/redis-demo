package com.lz.redis.demo.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author : liuze
 * @date: 2022/10/27 14:31
 **/
@TableName(value = "foreign_record_frank")
@Data
public class ForeignRecord {
    private String id;
    private String recordDate;
    private int count;
}
