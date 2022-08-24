package com.lz.redis.demo.model.entity.mongo;


import com.lz.redis.demo.annotatiion.MongodbEnc;
import com.lz.redis.demo.vo.BaseMongoEntity;
import lombok.*;

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
@MongodbEnc
public class TestChildDO extends BaseMongoEntity {

    private static final long serialVersionUID = 1L;


    private String childId;

    /**
     * 名称
     */
    @MongodbEnc
    private String name;




    public static final String FIELD_CHILD_ID = "childId";
    public static final String FIELD_NAME = "name";

}
