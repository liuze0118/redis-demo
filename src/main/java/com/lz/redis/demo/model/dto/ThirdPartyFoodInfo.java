package com.lz.redis.demo.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 第三方食物信息
 * @author: zyj
 * @email: zhaoyujie@jiankangyouyi.com
 * @date: 2021/10/9 10:06 上午
 */
@Data
public class ThirdPartyFoodInfo implements Serializable {
    /**
     * 食物id
     */
    private String foodId;
    /**
     * 食物名称
     */
    private String foodName;

    /**
     * 食物数量
     */
    private int value;

    /**
     * 食物热量
     */
    private String heat;

    /**
     * 食物图片
     */
    private String imageUrl;

    /**
     * 数量单位
     */
    private String unit;

    /**
     * 扩展单位
     */
    private List<ThirdPartyExtensionUnit> extensionUnits;


    /**
     * 第三方扩展单位
     */
    @Data
    public static class ThirdPartyExtensionUnit {
        private String unit;
        private String unitValue;
        private Integer unitSize;
    }
}
