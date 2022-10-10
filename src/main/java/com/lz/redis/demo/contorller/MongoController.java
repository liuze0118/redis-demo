package com.lz.redis.demo.contorller;

import com.lz.redis.demo.ilicense.LicenseVerify;
import com.lz.redis.demo.vo.GadgetsMenuDO;
import com.mongodb.client.result.DeleteResult;
import com.sun.istack.internal.NotNull;
import de.schlichtherle.license.LicenseContentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author : liuze
 * @date: 2022/6/29 18:11
 **/
@Slf4j
@RestController
@RequestMapping("/mongo")
public class MongoController {

    @Autowired
    @Qualifier(value = "mongoTemplate")
    private MongoTemplate mongoTemplate;

    @Resource
    private LicenseVerify licenseVerify;


    @GetMapping("/test")
    public ResponseEntity<String> testMongo() throws LicenseContentException {
        licenseVerify.verify();
        Criteria criteria = Criteria.where(GadgetsMenuDO.FIELD_NAME).is("改善目标");
        GadgetsMenuDO one = mongoTemplate.findOne(new Query(criteria), GadgetsMenuDO.class);
        String name = one.getName();
        return ResponseEntity.ok().body(name);
    }

    @GetMapping("/delete/tradeEnroll/{date}")
    public ResponseEntity<String> deleteTradeEnroll(@NotNull @PathVariable String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss sss");
        try {
            Date date1 = sdf.parse(date);
            //sdf.parse("2021-10-02 08:00:00 000")
            Criteria criteria = Criteria.where("createTime").lt(date1);
            Query query = new Query(criteria);
            long count = mongoTemplate.count(query, "trade.enroll");
            DeleteResult remove = mongoTemplate.remove(query, "trade.enroll");
            long deletedCount = remove.getDeletedCount();
            log.info("query count:{}  | delete count:{}",count,deletedCount);
            //Document document = mongoTemplate.executeCommand("db.getCollection('trade.enroll').find({\"createTime\" :{$lt:ISODate(\"2021-10-02T00:00:00.000Z\")}}).count()");
            return ResponseEntity.ok("删除数据成功,查询到数据:"+count + ",删除数据:" + deletedCount);
        } catch (ParseException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("日期格式错误");
        }
    }

}
