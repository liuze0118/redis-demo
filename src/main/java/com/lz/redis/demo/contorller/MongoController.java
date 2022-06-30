package com.lz.redis.demo.contorller;

import com.lz.redis.demo.vo.GadgetsMenuDO;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author : liuze
 * @date: 2022/6/29 18:11
 **/
@RestController
@RequestMapping("/mongo")
public class MongoController {

    @Resource
    private MongoTemplate mongoTemplate;

    @GetMapping("/test")
    public ResponseEntity<String> testMongo(){
        Criteria criteria = Criteria.where(GadgetsMenuDO.FIELD_NAME).is("改善目标");
        GadgetsMenuDO one = mongoTemplate.findOne(new Query(criteria), GadgetsMenuDO.class);
        String name = one.getName();
        return ResponseEntity.ok().body(name);
    }

}
