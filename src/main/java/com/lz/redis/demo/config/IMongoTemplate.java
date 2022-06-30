package com.lz.redis.demo.config;

import com.lz.redis.demo.annotatiion.MongodbEnc;
import com.lz.redis.demo.utils.SM4Utils;
import com.mongodb.BasicDBList;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class IMongoTemplate extends MongoTemplate {

    public IMongoTemplate(MongoDatabaseFactory mongoDbFactory, MongoConverter mongoConverter) {
        super(mongoDbFactory, mongoConverter);
    }

    @Override
    public <T> List<T> find(Query query, Class<T> entityClass) {
        return super.find(query, entityClass);
    }

    @Override
    public <T> List<T> find(Query query, Class<T> entityClass, String collectionName) {
        Query queryEnc = encodeParam(query, entityClass);
        return super.find(queryEnc, entityClass, collectionName);
    }

    @Override
    public long count(Query query, Class<?> entityClass, String collectionName) {
        Query queryEnc = encodeParam(query, entityClass);
        return super.count(queryEnc, entityClass, collectionName);
    }

    @Override
    public <T> T findOne(Query query, Class<T> entityClass) {
        return super.findOne(query, entityClass);
    }

    @Override
    public <T> T findOne(Query query, Class<T> entityClass, String collectionName) {
        Query queryEnc = encodeParam(query, entityClass);
        return super.findOne(queryEnc, entityClass, collectionName);
    }


    public <T> T findAndModify(Query query, Update update, Class<T> entityClass) {
        return super.findAndModify(query, update, entityClass);
    }

    public <T> T findAndModify(Query query, Update update, FindAndModifyOptions options, Class<T> entityClass) {
        Query queryEnc = encodeParam(query, entityClass);
        return super.findAndModify(queryEnc, update, options, entityClass);
    }

    private Query encodeParam(Query query, Class entityClass){
        if(entityClass.isAnnotationPresent(MongodbEnc.class)){
            Field[] fields = entityClass.getDeclaredFields();
            List<Field> fieldList = Arrays.asList(fields);
            List<String> encList = fieldList.stream()
                    .filter(field -> field.isAnnotationPresent(MongodbEnc.class))
                    .map(field -> field.getName())
                    .collect(Collectors.toList());
            Query queryEnc = new Query();
            AtomicInteger index = new AtomicInteger();
            Criteria[] criteria = handleCriteria(query.getQueryObject(), encList, index, new Criteria[1]);
            queryEnc.addCriteria(criteria[0]);
            queryEnc.limit(query.getLimit());
            queryEnc.skip(query.getSkip());
            if(query.getSortObject() != null && query.getSortObject().size() > 0){
                Sort sort = handleSort(query.getSortObject());
                queryEnc.with(sort);
            }
            query = queryEnc;
        }
        return query;
    }

    private Sort handleSort(Document document){
        Sort[] sort = {null};
        document.forEach((k,v)->{
            Sort.Direction direction = Integer.parseInt(v.toString()) == -1 ? Sort.Direction.DESC : Sort.Direction.ASC;
            if(sort[0] == null){
                sort[0] = Sort.by(direction,k);
            }else{
                sort[0] = sort[0].and(Sort.by(direction,k));
            }
        });
        return sort[0];
    }

    private Criteria[] handleCriteria(Document document, List<String> encList,AtomicInteger index,Criteria[] criteria) {
        document.forEach((k,v)->{
            if("$or".equals(k)){
                if(v instanceof BasicDBList){
                    Criteria [] criteriaArray = new Criteria[((BasicDBList) v).size()];
                    int innerIndex = 0 ;
                    for (Object obj : ((BasicDBList) v)) {
                        Document doc = (Document) obj;
                        Criteria[] criteriaOr = handleCriteria(doc, encList, new AtomicInteger(), new Criteria[1]);
                        criteriaArray[innerIndex] = criteriaOr[0];
                        innerIndex++;
                    }
                    if( criteria[0] == null){
                        criteria[0] = new Criteria().orOperator(criteriaArray);
                    }
                }
            }else{
                if(encList.contains(k)){
                    String val = (String)v;
                    v = SM4Utils.encryptSm4(val);
                }
                if(index.get() == 0){
                    criteria[0] = Criteria.where(k).is(v);
                }else{
                    criteria[0].and(k).is(v);
                }
                index.set(index.get() + 1);
            }
        });
        return criteria;
    }

}
