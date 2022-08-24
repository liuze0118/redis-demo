package com.lz.redis.demo.model.entity.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;

/**
 * 分页对象
 *
 * @author yangsongbo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Page implements Serializable {

    /**
     * 每页条数，必选，必须大于0
     */
//    @NotNull(message = "pageSize不能为空")
//    @Min(value = 1, message = "pageSize必须大于0")
    private Integer pageSize;

    /**
     * 页码，必须大于
     */
//    @NotNull(message = "pageNum不能为空")
//    @Min(value = 1, message = "pageNum必须大于0")
    private Integer pageNum;


    public int start() {
        return (pageNum - 1) * pageSize;
    }

}
