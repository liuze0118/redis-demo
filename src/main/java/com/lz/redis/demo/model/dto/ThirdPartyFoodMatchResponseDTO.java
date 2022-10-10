package com.lz.redis.demo.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 第三方配餐返回vo
 * @author: zyj
 * @email: zhaoyujie@jiankangyouyi.com
 * @date: 2021/10/9 10:01 上午
 */
@Data
public class ThirdPartyFoodMatchResponseDTO implements Serializable {


    /**
     * 早餐结果
     */
    private List<ThirdPartyFoodInfo> breakfast=new ArrayList<>();

    /**
     * 午餐结果
     */
    private List<ThirdPartyFoodInfo> lunch=new ArrayList<>();
    /**
     * 晚餐结果
     */
    private List<ThirdPartyFoodInfo> dinner=new ArrayList<>();

    /**
     * 早+餐结果
     */
    private List<ThirdPartyFoodInfo> breakfastAddition;
    /**
     * 午+餐结果
     */
    private List<ThirdPartyFoodInfo> lunchAddition;

    /**
     * 晚+餐结果
     */
    private List<ThirdPartyFoodInfo> dinnerAddition;

    /**
     * 配餐营养元素占比<p/>
     * eg:15-20
     */
    private NutrientMap nutrientMap;

    /**
     * 营养原则
     */
    private String dietaryPrinciple="1.限制总能量的摄入\n" + "2.限制脂肪的摄入\n" + "3.适当减少碳水化合物的摄入\n" + "4.注意补充优质蛋白质，适量吃鱼、禽、蛋、瘦肉\n" + "5.补充充足的维生素、矿物质\n" + "6.养成良好的饮食习惯";

    /**
     * 管理目标推荐
     */
    private String managementRecommendation="减脂";
    /**
     * 食谱对象
     */
    @Data
    public static class FoodAddition implements Serializable {
        /**
         * 配餐的食物
         */
        private List<ThirdPartyFoodInfo> foodList=new ArrayList<>();
    }
    /**
     * 三大产能占比<p/>
     * eg:15-20
     */
    @Data
    public static class NutrientMap implements Serializable {
        /**
         * 碳水化物
         */
        private String carbohydrate;
        /**
         * 蛋白质
         */
        private String protein;
        /**
         * 脂肪
         */
        private String fat;
    }
}
