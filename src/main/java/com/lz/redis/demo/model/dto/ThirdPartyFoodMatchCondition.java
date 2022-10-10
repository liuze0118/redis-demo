package com.lz.redis.demo.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 配餐条件
 * @author: zyj
 * @email: zhaoyujie@jiankangyouyi.com
 * @date: 2021/10/9 9:47 上午
 */
@Data
public class ThirdPartyFoodMatchCondition implements Serializable {

    /**
     * 生日
     */
    private String birthday;

    /**
     * 身高
     */

    private String height;

    /**
     * 体重
     */

    private String weight;

    /**
     * 性别
     */
    private String gender;

    /**
     * 口味
     */
    private String taste;
    /**
     * 民族
     */
    private String nation;
    /**
     * 地区
     */
    private String region;

    /**
     * 加餐列表，没有的话默认三餐<p/>
     * 1早2午3晚4早+5午+6晚+
     */
    private List<String> extraMealList;

    private List<NoEatFoodInfo> notEatFoodList;

    /**
     * 管理目标<p/>
     * 1减脂2增肌3保持健康4塑形
     */
    private String management;

    /**
     * 摄入能量
     */
    private Integer energy;

    /**
     * 是否强制配餐
     */
    private Boolean reMatch;

    /**
     * 不吃食物信息
     */
    @Data
    public static class NoEatFoodInfo implements Serializable {
        private String foodId;
        private String foodType;
        private String foodName;
        private int value;
        private int heat;
        private String imageUrl;
        private String unit;
        private String minQuantity;
        private String minQuantityEnergy;

    }
}
