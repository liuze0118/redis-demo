package com.lz.redis.demo.model.dto;

import lombok.*;


/**
 * @description: 第三方配餐请求dto
 * @author: zyj
 * @email: zhaoyujie@jiankangyouyi.com
 * @date: 2021/10/9 9:44 上午
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThirdPartyFoodMatchRequestDTO extends AbstractHttpApiRequestDTO {

    /**
     * 用户唯一标志
     */
    private String userId;

    /**
     * 配餐日期，不传递默认当天
     */
    private String recipesDate;

    /**
     * 配餐条件
     */
    private ThirdPartyFoodMatchCondition foodMatchConditions;


}
