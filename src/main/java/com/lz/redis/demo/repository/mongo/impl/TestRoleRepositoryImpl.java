package com.lz.redis.demo.repository.mongo.impl;


import com.lz.redis.demo.model.entity.mongo.TestRoleDO;
import com.lz.redis.demo.repository.mongo.TestRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author : liuze
 * @date: 2022/8/12 11:38
 **/
@Repository
public class TestRoleRepositoryImpl extends BaseMongoImpl<TestRoleDO> implements TestRoleRepository {
    @Override
    @Qualifier(value = "mongoTemplate")
    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        super.setMongoTemplate(mongoTemplate);
    }
}
