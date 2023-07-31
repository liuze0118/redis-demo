package com.lz.redis.demo.repository;

import org.apache.lucene.search.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

//@NoRepositoryBean
public interface PagingAndSortingRepository<T,ID> extends CurdRepository<T,ID> {
    Iterable<T> findAll(Sort sort);

    Page<T> findAll(Pageable pageable);
}
