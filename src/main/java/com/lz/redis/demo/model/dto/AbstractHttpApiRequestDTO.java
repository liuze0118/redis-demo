package com.lz.redis.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Http 请求DTO
 *
 * @author yangsongbo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractHttpApiRequestDTO {

    private static final long serialVersionUID = 1L;

    public static final String FIELD_BIZ_TYPE = "bizType";

    public static final String FIELD_APP_ID = "appId";

    public static final String FIELD_IP = "ip";


    private String bizType;


    private String ip;


    private String appId;

}
