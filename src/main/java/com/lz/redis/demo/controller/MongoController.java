package com.lz.redis.demo.controller;

import com.lz.redis.demo.dao.ForeignRecordDao;
import com.lz.redis.demo.ilicense.LicenseVerify;
import com.lz.redis.demo.model.entity.mongo.TestChildDO;
import com.lz.redis.demo.model.entity.mongo.TestMenuDO;
import com.lz.redis.demo.model.entity.mongo.TestRoleDO;
import com.lz.redis.demo.vo.ForeignRecord;
import com.lz.redis.demo.vo.GadgetsMenuDO;
import com.mongodb.client.result.DeleteResult;
import com.sun.istack.internal.NotNull;
import de.schlichtherle.license.LicenseContentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author : liuze
 * @date: 2022/6/29 18:11
 **/
@Slf4j
//@RestController
@RequestMapping("/mongo")
public class MongoController {

    @Autowired
    @Qualifier(value = "mongoTemplate")
    private MongoTemplate mongoTemplate;

    @Autowired
    private ForeignRecordDao recordDao;

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

    @GetMapping("/test1")
    public ResponseEntity<String> test1Mongo() throws LicenseContentException {
        TestChildDO childDO = TestChildDO.builder().name("Test-Child-10-"+ 12).build();
        TestMenuDO testMenuDO1 = TestMenuDO.builder().name("测试菜单-10-"+ 12).childDO(childDO).build();

        TestMenuDO save = mongoTemplate.save(testMenuDO1);
        return ResponseEntity.ok().body("name");
    }

    @GetMapping("/connection/test")
    public ResponseEntity<String> testConnectionMongo() throws LicenseContentException {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(()->{
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                TestChildDO childDO = TestChildDO.builder().name("Test-Child-10-"+ finalI).build();
                TestMenuDO testMenuDO1 = TestMenuDO.builder().name("测试菜单-10-"+ finalI).childDO(childDO).build();

                TestMenuDO save = mongoTemplate.save(testMenuDO1);
                if(null != save){
                    System.out.println("当前线程id为:" + Thread.currentThread().getName() + "name:" + save.getName());
                }else{
                    System.out.println("当前线程id为:" + Thread.currentThread().getName());
                }
            }).start();
            countDownLatch.countDown();
        }
        return ResponseEntity.ok().body("OK");
    }
    @GetMapping("/connection/test1")
    public ResponseEntity<String> testConnection1Mongo() throws LicenseContentException {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(()->{
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Criteria criteria = Criteria.where(GadgetsMenuDO.FIELD_NAME).is("改善目标");
                GadgetsMenuDO one = mongoTemplate.findOne(new Query(criteria), GadgetsMenuDO.class);
                if(null != one){
                    System.out.println("当前线程id为:" + Thread.currentThread().getName() + "name:" + one.getName());
                }else{
                    System.out.println("当前线程id为:" + Thread.currentThread().getName());
                }
            }).start();
            countDownLatch.countDown();
        }
        return ResponseEntity.ok().body("OK");
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

    @GetMapping("/delete/foreignRecord/{date}")
    public ResponseEntity<String> deleteForeignRecord(@NotNull @PathVariable String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss sss");
        try {
            Date date1 = sdf.parse(date);
//            Aggregation aggregation=Aggregation.newAggregation(
//                    //添加过滤条件
//                    Aggregation.match(criteria),
//                    //查询字段，andExpression（xx）.as(yy) 表示将查询字段中的yy替换为xx
//                    //这里用到了 month函数,需要替换
//                    Aggregation.project("createTime","userId").andExpression("{$month: '$createTime'}").as("createTime"),
//                    //根据createTime分组
//                    //first为 当前列聚合时取多条数据中的第一条
//                    Aggregation.group("createTime").first("createTime").as("monthNum").count().as("monthCount").first("userId").as("userId")
//            );
            Criteria criteria = Criteria.where("createTime").lt(date1);
            Aggregation aggregation=Aggregation.newAggregation(
//                    Aggregation.match(criteria),
//                    Aggregation.project("recordDate"),
                    //first为 当前列聚合时取多条数据中的第一条
                    Aggregation.group("recordDate").first("recordDate").as("recordDate").count().as("count")
            );
            AggregationResults<ForeignRecord> aggregate = mongoTemplate.aggregate(aggregation, "record.huawei.foreign", ForeignRecord.class);
            List<ForeignRecord> mappedResults = aggregate.getMappedResults();
            mappedResults.stream().forEach(record->{
                recordDao.insert(record);
            });
//            Query query = new Query(criteria);
//            long count = mongoTemplate.count(query, "trade.enroll");
//            DeleteResult remove = mongoTemplate.remove(query, "trade.enroll");
//            long deletedCount = remove.getDeletedCount();
//            log.info("query count:{}  | delete count:{}",count,deletedCount);
            //Document document = mongoTemplate.executeCommand("db.getCollection('trade.enroll').find({\"createTime\" :{$lt:ISODate(\"2021-10-02T00:00:00.000Z\")}}).count()");
            return ResponseEntity.ok("删除数据成功,查询到数据:"+1 + ",删除数据:" + 1);
        } catch (ParseException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("日期格式错误");
        }
    }

}
