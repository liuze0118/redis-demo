package com.lz.redis.demo.vo;

import com.lz.redis.demo.annotatiion.MongodbEnc;
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
@Document(collection = "v3.gadgets.menu")
@MongodbEnc
public class GadgetsMenuDO extends BaseMongoEntity{

    private static final long serialVersionUID = 1L;


    private String menuId;

    /**
     * 名称
     */
    @MongodbEnc
    private String name;
    /**
     * 名称
     */
    private String imageUrl;
    /**
     * 规则
     */
    private String rule;
    /**
     * 级别
     *
     */
    private String level;

    private String content;

    private Integer sort;

    public static final String FIELD_SORT = "sort";
    public static final String FIELD_MENUID = "menuId";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_IMAGEURL = "imageUrl";
    public static final String FIELD_RULE = "rule";
    public static final String FIELD_LEVEL = "level";
    public static final String FIELD_CONTENT = "content";
}
