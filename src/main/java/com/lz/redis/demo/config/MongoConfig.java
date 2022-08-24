package com.lz.redis.demo.config;

import com.lz.redis.demo.config.converter.BigDecimalToDecimal128Converter;
import com.lz.redis.demo.config.converter.Decimal128ToBigDecimalConverter;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.util.ArrayList;
import java.util.List;

/**
 * mongo数据源配置信息
 *
 * @author liuze
 **/
@Configuration
@ConfigurationProperties(prefix = "spring.data.mongodb")
@Getter
@Setter
public class MongoConfig{
    private MongoDatabaseFactory factory;

    protected String uri;

    @Bean(name = "mongoTemplate")
    public MongoTemplate mongoTemplate(MongoDatabaseFactory factory) {
        this.factory = new SimpleMongoClientDatabaseFactory(this.uri);
        return new IMongoTemplate(factory, this.MappingMongoConverter());
    }

    private MappingMongoConverter MappingMongoConverter() {
        List<Converter> converters = new ArrayList<>();
        converters.add(new Decimal128ToBigDecimalConverter());
        converters.add(new BigDecimalToDecimal128Converter());
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(this.factory);
        MongoCustomConversions conversions = new MongoCustomConversions(converters);
        MongoMappingContext mappingContext = new MongoMappingContext();
        mappingContext.setSimpleTypeHolder(conversions.getSimpleTypeHolder());
        mappingContext.afterPropertiesSet();
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mappingContext);
        converter.setCustomConversions(conversions);
        converter.setCodecRegistryProvider(this.factory);
        converter.afterPropertiesSet();
        return converter;
    }

}
