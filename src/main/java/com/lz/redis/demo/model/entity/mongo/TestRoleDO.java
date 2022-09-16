package com.lz.redis.demo.model.entity.mongo;


import com.bol.secure.Encrypted;
import com.lz.redis.demo.annotatiion.MongodbEnc;
import com.lz.redis.demo.vo.BaseMongoEntity;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * 描述
 *
 * @author qxy
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "app.a.role")
@MongodbEnc
public class TestRoleDO extends BaseMongoEntity {

    private static final long serialVersionUID = 1L;


    private String roleId;

    /**
     * 名称
     */
    @MongodbEnc
    private String roleName;

    private Integer auth;


    @Encrypted
    private BigDecimal val;

    private TestMenuDO menuDO;

    public static final String FIELD_MENUID = "roleId";
    public static final String FIELD_NAME = "roleName";
    public static final String FIELD_AUTH = "auth";
    public static final String FIELD_VAL = "val";
    public static final String FIELD_MENUDO_NAME = "menuDO.name";
    public static final String FIELD_MENUDO_CHILD_NAME = "menuDO.childDO.name";

    
}
