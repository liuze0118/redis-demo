package com.lz.redis.demo.model.entity.mysql;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author : liuze
 * @date: 2022/9/22 14:15
 **/
@Data
@Builder
@TableName("match_result")
public class MatchResult {

    private int id;

    private String management;

    private int status;

    private int energy;

    private String height;

    private String weight;

    private String spendTime;

    private String batchNo;

    private String mealKey;

    private String breakFastName;

    private String lunName;

    private String superName;

    private Date createTime;


}
