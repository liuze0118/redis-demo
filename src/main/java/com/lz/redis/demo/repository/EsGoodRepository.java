package com.lz.redis.demo.repository;

import com.lz.redis.demo.vo.Good;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EsGoodRepository extends ElasticsearchRepository<Good,Long> {
    Page<Good> findByBrandName(String brandName, Pageable pageable);
}
