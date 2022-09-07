package com.lz.redis.demo.model.entity.mysql;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : liuze
 * @date: 2022/8/25 10:49
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "health_interface_info",autoResultMap = true)
public class HealthInterfaceInfo {
    //private int id;
    private String module;
    private String serviceName;
    private String version;
    private String docUrl;
    private String url;
    @TableField("describe_str")
    private String describe;
    private String department;
    @TableField("chain_str")
    private String chain;
    private String project;
    private String remark;
}
