package com.lz.redis.demo.repository.mongo.impl;


import com.lz.redis.demo.model.entity.mongo.Page;
import com.lz.redis.demo.model.entity.mongo.PageResult;
import com.lz.redis.demo.repository.mongo.BaseMongo;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * mongo操作模版
 *
 * @author liubin
 */
public class BaseMongoImpl<T> implements BaseMongo<T> {
    /**
     * 默认分页信息
     */
    private static final int DEFAULT_SKIP = 1;
    private static final int DEFAULT_LIMIT = 10;

    private static final String ID_FIELD = "_id";


    protected MongoTemplate mongoTemplate;


    /**
     * 通过条件查询实体(集合)，可以指定条件和集合名
     */
    @Override
    public List<T> find(Query query, Class<T> entityClass) {
        Assert.notNull(entityClass, "this mongoBean is required; it must not be null");
        Assert.notNull(query, "this query is required; it must not be null");
        return mongoTemplate.find(query, entityClass);
    }

    @Override
    public List<T> findAll(Class<T> entityClass) {
        Assert.notNull(entityClass, "this mongoBean is required; it must not be null");
        return this.mongoTemplate.findAll(entityClass);
    }

    /**
     * 可以指定条件和集合名以及返回集合实体类
     */
    @Override
    public T findOne(Query query, Class<T> entityClass) {
        Assert.notNull(entityClass, "this mongoBean is required; it must not be null");
        Assert.notNull(query, "this query is required; it must not be null");
        return mongoTemplate.findOne(query, entityClass);
    }

    @Override
    public T findAndModify(Query query, Update update, Class<T> entityClass) {
        Assert.notNull(entityClass, "this mongoBean is required; it must not be null");
        Assert.notNull(query, "this query is required; it must not be null");
        Assert.notNull(update, "this update is required; it must not be null");
        return mongoTemplate.findAndModify(query, update, entityClass);
    }

    @Override
    public T findAndModify(Query query, Update update, FindAndModifyOptions options, Class<T> entityClass) {
        Assert.notNull(entityClass, "this mongoBean is required; it must not be null");
        Assert.notNull(query, "this query is required; it must not be null");
        Assert.notNull(update, "this update is required; it must not be null");
        return mongoTemplate.findAndModify(query, update, options, entityClass);
    }

    /**
     * 保存一个对象到mongodb
     */
    @Override
    public T insert(T entity) {
        Assert.notNull(entity, "this mongoBean is required; it must not be null");
        mongoTemplate.insert(entity);
        return entity;
    }

    /**
     * 批量新增
     */
    @Override
    public void insertBatch(List<T> t, Class<T> entityClass) {
        Assert.notNull(entityClass, "this entityClass is required; it must not be null");
        Assert.notEmpty(t, "insert list must not be empty: it must contain at least 1 element");
        mongoTemplate.insert(t, entityClass);
    }

    @Override
    public T save(T mongoBean) {
        Assert.notNull(mongoBean, "this mongoBean is required; it must not be null");
        return this.mongoTemplate.save(mongoBean);
    }

    /**
     * 按条件删除
     */
    @Override
    public DeleteResult remove(Query query, Class<?> entityClass) {
        Assert.notNull(entityClass, "this entityClass is required; it must not be null");
        return this.mongoTemplate.remove(query, entityClass);
    }


    /**
     * 通过ID获取记录,并且指定了集合名(表的意思)
     */
    @Override
    public T findById(String id, Class<T> entityClass) {
        Assert.notNull(entityClass, "this entityClass is required; it must not be null");
        Assert.notNull(id, "this id is required; it must not be null");
        return mongoTemplate.findById(id, entityClass);
    }

    @Override
    public <O> AggregationResults<O> aggregate(Aggregation aggregation, Class<?> inputType, Class<O> outputType) {
        return this.mongoTemplate.aggregate(aggregation, inputType, outputType);
    }

    @Override
    public UpdateResult update(Query query, Update update, Class<?> entityClass) {
        Assert.notNull(entityClass, "this entityClass is required; it must not be null");
        Assert.notNull(query, "this query is required; it must not be null");
        Assert.notNull(update, "this update is required; it must not be null");
        return this.mongoTemplate.updateMulti(query, update, entityClass);
    }

    @Override
    public UpdateResult updateFirst(Query query, Update update, Class<?> entityClass) {
        Assert.notNull(entityClass, "this entityClass is required; it must not be null");
        Assert.notNull(query, "this query is required; it must not be null");
        Assert.notNull(update, "this update is required; it must not be null");
        return this.mongoTemplate.updateFirst(query, update, entityClass);
    }

    @Override
    public UpdateResult upsert(Query query, Update update, Class<?> entityClass) {
        Assert.notNull(entityClass, "this entityClass is required; it must not be null");
        Assert.notNull(query, "this query is required; it must not be null");
        Assert.notNull(update, "this update is required; it must not be null");
        return this.mongoTemplate.upsert(query, update, entityClass);
    }


//    @Deprecated
//    @Override
//    public com.jiankangyouyi.cloud.core.base.entity.Page<T> findPage(
//            Integer pageNum, Integer pageSize, Query query, Class<T> entityClass) {
//
//        Assert.notNull(query, "this query is required; it must not be null");
//        Assert.notNull(entityClass, "this entityClass is required; it must not be null");
//
//        if (pageSize == null || pageSize <= 0) {
//            pageSize = DEFAULT_LIMIT;
//        }
//
//        if (pageNum == null || pageNum < 1) {
//            pageNum = DEFAULT_SKIP;
//        }
//
//        //获取总数
//        long total = this.count(query, entityClass);
//
//        if (total == 0) {
//            return new com.jiankangyouyi.cloud.core.base.entity.Page<>(
//                    null, pageNum, pageSize, total);
//        }
//
//        query.limit(pageSize);
//        query.skip((pageNum - 1) * pageSize);
//
//        //将查询的数据设置进集合
//        return new com.jiankangyouyi.cloud.core.base.entity.Page<>(
//                this.find(query, entityClass), pageNum, pageSize, total);
//    }

    @Override
    public PageResult<T> findPage(Page page, Query query, String[] fields, Class<T> entityClass) {
        Assert.notNull(page, "this page is required; it must not be null");
        Assert.notNull(query, "this query is required; it must not be null");
        Assert.notNull(entityClass, "this entityClass is required; it must not be null");

        Integer pageNum = page.getPageNum();
        Integer pageSize = page.getPageSize();
        //获取总数
        long totalCount = this.count(query, entityClass);

        if (totalCount == 0) {
            return new PageResult<>(
                    new PageResult.PageInfo(pageNum, pageSize, totalCount, 0),
                    null);
        }

        if (ArrayUtils.isNotEmpty(fields)) {
            if (ArrayUtils.indexOf(fields, ID_FIELD) < 0) {
                fields = ArrayUtils.add(fields, ID_FIELD);
            }
            for (String field : fields) {
                query.fields().include(field);
            }
        }

        query.limit(pageSize);
        query.skip(page.start());

        List<T> rowData = this.find(query, entityClass);

        int dataSize = CollectionUtils.isEmpty(rowData) ? 0 : rowData.size();
        //将查询的数据设置进集合
        return new PageResult<>(
                new PageResult.PageInfo(pageNum, pageSize, totalCount, dataSize), rowData);
    }

    @Override
    public long count(Query query, Class<T> entityClass) {
        return mongoTemplate.count(query, entityClass);
    }

    @Override
    public List<T> sum(String collectionName, MatchOperation match, GroupOperation group, Class<T> outputType) {
        Aggregation aggregation = Aggregation.newAggregation(match, group);
        AggregationResults<T> aggRes = mongoTemplate.aggregate(aggregation, collectionName, outputType);
        return aggRes.getMappedResults();
    }

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}
