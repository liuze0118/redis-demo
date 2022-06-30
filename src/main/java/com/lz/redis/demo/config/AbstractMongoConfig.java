package com.lz.redis.demo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;

/**
 * mongo配置信息
 *
 * @author liuze
 **/
@Getter
@Setter
public abstract class AbstractMongoConfig {

    protected String uri;

    public MongoDatabaseFactory mongoDbFactory() {
        return new SimpleMongoClientDbFactory(this.uri);
    }
}
