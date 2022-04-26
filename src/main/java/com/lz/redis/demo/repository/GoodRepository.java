package com.lz.redis.demo.repository;

import com.lz.redis.demo.vo.Good;
import org.apache.lucene.search.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.core.SearchHit;

import java.util.List;

public interface GoodRepository extends CurdRepository<Good,Long> {

    List<Good> findGoodsByName(String name);

    @Highlight(fields = {
            @HighlightField(name = "brandName"),
            @HighlightField(name = "name")
    })
    List<SearchHit<Good>> findGoodsByNameOrBrandName(String name, String brandName);

    Page<Good> findGoodsByBrandName(String brandName,Pageable pageable);

}
