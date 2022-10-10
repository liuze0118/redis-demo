package com.lz.redis.demo.model.dto;


import lombok.*;

import java.util.List;

/**
 * @description: 配餐请求dto
 * @author: zyj
 * @email: zhaoyujie@jiankangyouyi.com
 * @date: 2021/7/26 3:32 下午
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class APIMatchFoodV2RequestDTO extends AbstractHttpApiRequestDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */

    private String userId;
    /**
     * 体重 kg
     */
    private String weight;
    /**
     * 身高 cm
     * @length 3
     */
    private String height;
    /**
     * 出生年月日 yyyy-MM-dd
     */
    private String birthday;
    /**
     * 性别 1:男2:女
     * @length 1
     */
    private String gender;
    /**
     * 体力活动等级
     * @length 1
     */
    private String activityLevel;
    /**
     * 运动等级
     * @length 1
     */
    private String exerciseRiskLevel;

    /**
     * 疾病情况
     */
    private List<HealthProblemInfo> healthProblemInfos;

    /**
     * NULL 营养素状态信息 当健康问题包含 NUTRIENT_DEFICIENCY 时此处不可为空
     */
//    private List<NutrientStateInfo> nutrientStateInfos;

    /**
     * NUll 分娩日期,性别为女性时才能赋值 yyyy-MM-dd
     */
    private String deliveryDate;


    private String lactationStatus;

    /**
     * NULL 民族 （膳食使用时数据来源——字典——字典大类：食物库，字典子类：禁忌民族）
     */
    private String nation;
    /**
     * 所在的地域
     * （膳食使用时数据来源——字典——字典大类：食物库，字典子类：国家及地区）
     */
    private String area;

    /**
     * 喜好口味
     * （膳食使用时数据来源——字典——字典大类：食物库，字典子类：口味）
     */
    private List<String> taste;
    /**
     * 配餐日期
     */
    private String planningDate;

    /**
     * 是否强行重新配餐
     * @length 5
     */
    private String reMatch;



    private String type2DiabetesPharmacyCode;

    private int recommendedIntakeEnergy;
}
