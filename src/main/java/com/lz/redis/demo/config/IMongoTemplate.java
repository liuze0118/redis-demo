package com.lz.redis.demo.config;

import com.alibaba.fastjson.JSONObject;
import com.lz.redis.demo.annotatiion.MongodbEnc;
import com.lz.redis.demo.utils.SM4Utils;
import com.lz.redis.demo.vo.BaseMongoEntity;
import com.mongodb.BasicDBList;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;
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
        Query queryEnc = encodeParamRecursion(null,query, entityClass);
        return super.find(queryEnc, entityClass, collectionName);
    }

    @Override
    public long count(Query query, Class<?> entityClass) {
        return super.count(query, entityClass);
    }

    @Override
    public long count(Query query, Class<?> entityClass, String collectionName) {
        Query queryEnc = encodeParamRecursion(null,query, entityClass);
        return super.count(queryEnc, entityClass, collectionName);
    }

    @Override
    public <T> T findOne(Query query, Class<T> entityClass) {
        return super.findOne(query, entityClass);
    }

    @Override
    public <T> T findOne(Query query, Class<T> entityClass, String collectionName) {
        Query queryEnc = encodeParamRecursion(null,query, entityClass);
        return super.findOne(queryEnc, entityClass, collectionName);
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

    private Query encodeParamRecursion(String preKey, Query query, Class entityClass){
        if(null == entityClass){
            return query;
        }
        if(entityClass.isAnnotationPresent(MongodbEnc.class)){
            Field[] fields = entityClass.getDeclaredFields();
            List<Field> fieldList = Arrays.asList(fields);
            fieldList.stream().forEach(field -> {
                if(field.isAnnotationPresent(MongodbEnc.class)){
                    handleEnc(query, preKey==null?field.getName():preKey+"." + field.getName());
                }else if (field.getType().getSuperclass() == BaseMongoEntity.class && field.getType().isAnnotationPresent(MongodbEnc.class)){
                    Class clazz = field.getType();
                    encodeParamRecursion(preKey==null?field.getName():preKey+"." + field.getName(),query,clazz);
                }
            });
        }
        return query;
    }

    private void handleEnc(Query query, String key) {
        try {
            Field criteria = query.getClass().getDeclaredField("criteria");
            criteria.setAccessible(true);
            LinkedHashMap<String, CriteriaDefinition> criteriaMap = (LinkedHashMap<String, CriteriaDefinition>) criteria.get(query);
            String json = JSONObject.toJSONString(criteriaMap);
            LinkedHashMap hashMap = JSONObject.parseObject(json, LinkedHashMap.class);
            criteriaMap.forEach((k,v)->{
                Field fieldChain;
                try {
                    fieldChain = v.getClass().getDeclaredField("criteriaChain");
                    fieldChain.setAccessible(true);
                    List<Criteria> list = (List<Criteria>) fieldChain.get(v);
                    list.forEach(cr -> handleCriteriaChain(key, cr));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void handleCriteriaChain(String key, Criteria cr) {
        Field isValue;
        try {
            isValue = cr.getClass().getDeclaredField("isValue");
            isValue.setAccessible(true);
            if(isValue.get(cr).getClass() == BasicDBList.class){
                BasicDBList dbList = (BasicDBList) isValue.get(cr);
                dbList.forEach(o->{
                    ((Document)o).forEach((k1, v1)->{
                        if(k1.equals(key)){
                            if(v1.getClass() == Document.class){
                                ((Document)v1).forEach((k2, v2)->{
                                    if(k2.equals("$in")){
                                        List<String> arrayV = (List<String>)v2;
                                        for (int i = 0; i < arrayV.size(); i++) {
                                            String str = arrayV.get(i);
                                            if(!(str.startsWith("ENC(") && str.endsWith(")"))){
                                                arrayV.set(i, SM4Utils.encryptSm4(str));
                                            }
                                        }
                                    }
                                });
                            }else if(v1.getClass() == String.class &&  !(v1.toString().startsWith("ENC(") && v1.toString().endsWith(")"))){
                                ((Document) o).put(k1, SM4Utils.encryptSm4(v1+""));
                            }
                        }
                    });
                });
            }else if(isValue.get(cr).getClass() == String.class){
                String v2 = isValue.get(cr) + "";
                if(key.equals(cr.getKey()) && !(v2.startsWith("ENC(") && v2.endsWith(")"))){
                    isValue.set(cr,SM4Utils.encryptSm4(v2));
                }
            }else if(isValue.get(cr).getClass() == Object.class){
                if(key.equals(cr.getKey())){
                    Field criteria = cr.getClass().getDeclaredField("criteria");
                    criteria.setAccessible(true);
                    Object temp = criteria.get(cr);
                    if(temp.getClass() == LinkedHashMap.class){
                        LinkedHashMap cmap = (LinkedHashMap)temp;
                        cmap.forEach((k,v)->{
                            if(k.equals("$in")){
                                List<String> arrayV = (List<String>)v;
                                for (int i = 0; i < arrayV.size(); i++) {
                                    String str = arrayV.get(i);
                                    if(!(str.startsWith("ENC(") && str.endsWith(")"))){
                                        arrayV.set(i,SM4Utils.encryptSm4(str));
                                    }
                                }
                            }
                        });
                    }
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
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

    private Criteria[] handleCriteria(Document document, List<String> encList, AtomicInteger index, Criteria[] criteria) {
        document.forEach((k,v)->{
            if("$or".equals(k)){
                if(v instanceof BasicDBList){
                    Criteria[] criteriaArray = new Criteria[((BasicDBList) v).size()];
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
