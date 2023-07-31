package com.lz.redis.demo.repository;

import com.lz.redis.demo.vo.Good;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//extends ElasticsearchRepository<Good,Long>
public interface EsGoodRepository   {
    Page<Good> findByBrandName(String brandName, Pageable pageable);
}
