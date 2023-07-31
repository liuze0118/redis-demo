package com.lz.redis.demo.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.Optional;

//@NoRepositoryBean
//extends Repository<T,ID>
public interface CurdRepository<T,ID>  {
    <S extends T> S save(S entity);

    Optional<T> findById(ID primaryKey);

    Iterable<T> findAll();

    long count();

    void delete(T entity);

    boolean existsById(ID primaryKey);
}
