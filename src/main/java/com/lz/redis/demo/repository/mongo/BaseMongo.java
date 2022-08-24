package com.lz.redis.demo.repository.mongo;


import com.lz.redis.demo.model.entity.mongo.Page;
import com.lz.redis.demo.model.entity.mongo.PageResult;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 * Mongo基类
 *
 * @author liubin
 * @author yangsongbo
 */
public interface BaseMongo<T> {

    /**
     * 查询一批记录
     *
     * @param query       查询对象
     * @param entityClass 指定Mongo Entity Class
     * @return 查询结果
     */
    List<T> find(Query query, Class<T> entityClass);

    /**
     * 查询所有记录
     *
     * @param entityClass 指定Mongo Entity Class
     * @return 查询结果
     */
    List<T> findAll(Class<T> entityClass);

    /**
     * 查询一条记录，当存在多条匹配记录时，返回第一条
     *
     * @param query       查询对象
     * @param entityClass 指定Mongo Entity Class
     * @return 查询结果
     */
    T findOne(Query query, Class<T> entityClass);

    /**
     * 查询并更新记录
     *
     * @param query       查询对象
     * @param update      更新对象
     * @param entityClass 指定Mongo Entity Class
     * @return 返回更新前的数据，未查询到数据时，返回null
     */
    T findAndModify(Query query, Update update, Class<T> entityClass);

    /**
     * 查询并更新记录
     *
     * @param query       查询对象
     * @param update      更新对象
     * @param options     操作对象
     *                    returnNew :  是否返回更新后的数据
     *                    upsert : 未查询到数据时，是否执行保存操作
     *                    remove : 是否删除查询到的数据
     * @param entityClass 指定Mongo Entity Class
     * @return 返回查询结果
     */
    T findAndModify(Query query, Update update, FindAndModifyOptions options, Class<T> entityClass);

    /**
     * 插入一个对象到MongoDB
     *
     * @param entity Mongo Entity对象
     * @return 返回当前保存的对象
     */
    T insert(T entity);

    /**
     * 插入一批对象到MongoDB
     *
     * @param entityList  待保存的数据集合
     * @param entityClass 指定Mongo Entity Class
     */
    void insertBatch(List<T> entityList, Class<T> entityClass);

    /**
     * 插入或覆盖更新一条记录，覆盖更新时以_id来判断
     *
     * @param entity 操作的mongo对象
     * @return 保存的对象
     */
    T save(T entity);

    /**
     * 根据指定的条件进行物理删除
     *
     * @param query       查询对象
     * @param entityClass 指定Mongo Entity Class
     * @return 删除结果
     */
    DeleteResult remove(Query query, Class<?> entityClass);


    /**
     * 根据_id查询一条记录
     *
     * @param id          Mongo集合的_id
     * @param entityClass 指定Mongo Entity Class
     * @return 查询结果
     */
    T findById(String id, Class<T> entityClass);

    /**
     * 执行聚合操作
     *
     * @param aggregation 聚合对象
     * @param inputType   指定Mongo Entity Class
     * @param outputType  指定输出结果的Entity Class
     * @return 聚合操作结果
     */
    <O> AggregationResults<O> aggregate(Aggregation aggregation, Class<?> inputType, Class<O> outputType);


    /**
     * 根据条件更新一批记录
     *
     * @param query       查询对象
     * @param update      更新对象
     * @param entityClass 指定Mongo Entity Class
     * @return 更新结果
     */
    UpdateResult update(Query query, Update update, Class<?> entityClass);

    /**
     * 根据条件更新一条记录，查询到多条记录，更新第一条
     *
     * @param query       查询对象
     * @param update      更新对象
     * @param entityClass 指定Mongo Entity Class
     * @return 更新结果
     */
    UpdateResult updateFirst(Query query, Update update, Class<?> entityClass);


    /**
     * 根据查询条件，更新或插入一条记录
     *
     * @param query       查询对象
     * @param update      更新对象
     * @param entityClass 指定Mongo Entity Class
     * @return 处理结果
     */
    UpdateResult upsert(Query query, Update update, Class<?> entityClass);

    /**
     * 分页查询
     *
     * @param pageNum     页码
     * @param pageSize    每页条数
     * @param query       查询对象
     * @param entityClass 指定Mongo Entity Class
     * @return 分页结果
     */
//    com.jiankangyouyi.cloud.core.base.entity.Page<T> findPage(
//            Integer pageNum, Integer pageSize, Query query, Class<T> entityClass);


    /**
     * 分页查询
     *
     * @param page        分页信息
     * @param query       查询对象
     * @param fields      指定返回的属性，为空返回所有的属性
     * @param entityClass 指定Mongo Entity Class
     * @return 分页结果
     */
    PageResult<T> findPage(Page page, Query query, String[] fields, Class<T> entityClass);

    /**
     * 根据查询条件，返回记录数
     *
     * @param query       查询对象
     * @param entityClass 指定Mongo Entity Class
     * @return 匹配的记录数
     */
    long count(Query query, Class<T> entityClass);

    /**
     * 执行聚合操作
     *
     * @param collectionName 集合名称
     * @param match          查询对象
     * @param group          分组对象
     * @param outputType     指定返回的数据类型
     * @return 聚合操作结果
     * @deprecated 使用aggregate(...)
     */
    @Deprecated
    List<T> sum(String collectionName, MatchOperation match, GroupOperation group, Class<T> outputType);
}
