package com.lz.redis.demo.vo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.util.Date;

@Data
@Document(indexName = "goods_sku",refreshInterval = "0")
@Setting(
        sortFields = {"id"},
        sortModes = { Setting.SortMode.max},
        sortOrders = {Setting.SortOrder.desc}
)
public class Good {
    @Id
    @Field(type = FieldType.Long,store = true)
    private Long id;            // 主键Id
    @Field(type = FieldType.Text,store = true)
    private String name;        // 商品名称
    @Field(type = FieldType.Integer,store = true)
    private Integer price;      // 商品价格
    @Field(type = FieldType.Text,store = true,index = false)
    private String image;       // 商品图片src
    @Field(type = FieldType.Date,store = true,index = false)
    private Date createTime;    // 商品创建时间
    @Field(type = FieldType.Long,store = true,index = false)
    private Long spuId;         // Spu的Id
    @Field(type = FieldType.Keyword,store = true)
    private String categoryName;// 分类名称
    @Field(type = FieldType.Keyword,store = true)
    private String brandName;   // 品牌名称
    @Field(type = FieldType.Integer,store = true,index = false)
    private Integer saleNum;    // 销量

    public Good() {

    }

    public Good(Long id, String name, Integer price, String image, Date createTime, Long spuId, String categoryName, String brandName, Integer saleNum) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.createTime = createTime;
        this.spuId = spuId;
        this.categoryName = categoryName;
        this.brandName = brandName;
        this.saleNum = saleNum;
    }
}
