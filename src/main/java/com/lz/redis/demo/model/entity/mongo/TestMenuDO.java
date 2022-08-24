package com.lz.redis.demo.model.entity.mongo;


import com.lz.redis.demo.annotatiion.MongodbEnc;
import com.lz.redis.demo.vo.BaseMongoEntity;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

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
@Document(collection = "app.a.menu")
@MongodbEnc
public class TestMenuDO extends BaseMongoEntity {

    private static final long serialVersionUID = 1L;


    private String menuId;

    /**
     * 名称
     */
    @MongodbEnc
    private String name;

    private TestChildDO childDO;




    public static final String FIELD_MENUID = "menuId";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_CHILDDO_NAME = "childDO.name";


}
