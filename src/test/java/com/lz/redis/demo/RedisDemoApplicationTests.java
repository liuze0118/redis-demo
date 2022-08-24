package com.lz.redis.demo;

import com.lz.redis.demo.model.entity.mongo.TestChildDO;
import com.lz.redis.demo.model.entity.mongo.TestMenuDO;
import com.lz.redis.demo.model.entity.mongo.TestRoleDO;
import com.lz.redis.demo.repository.mongo.TestMenuRepository;
import com.lz.redis.demo.repository.mongo.TestRoleRepository;
import com.lz.redis.demo.service.RedisService;
import com.lz.redis.demo.vo.Good;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.concurrent.CountDownLatch;

@SpringBootTest
class RedisDemoApplicationTests {
    @Autowired
    private RedisService redisService;
    @Autowired
    private TestRoleRepository testRoleRepository;
    @Autowired
    private TestMenuRepository testMenuRepository;
    @Test
    void contextLoads() {
    }

    @Test
    public void cacheThreadTest(){
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Good good = redisService.testCache("test");
                System.out.println(good.getName());
            }).start();
            countDownLatch.countDown();
        }
    }

    @Test
    public void testENc(){

        List<TestRoleDO> all = testRoleRepository.findAll(TestRoleDO.class);
        System.out.println(all.size());

        Criteria criteria0 = Criteria.where(TestRoleDO.FIELD_NAME).is("测试角色-3");
        List<TestRoleDO> testRoleDOS0 = testRoleRepository.find(new Query(criteria0), TestRoleDO.class);
        System.out.println(testRoleDOS0.size());

        Criteria criteria = Criteria.where(TestRoleDO.FIELD_MENUDO_CHILD_NAME).is("Test-Child-4");
        List<TestRoleDO> testRoleDOS = testRoleRepository.find(new Query(criteria), TestRoleDO.class);
        System.out.println(testRoleDOS.size());
        System.out.println("-------");
    }

    @Test
    public void testInsertENc(){
//        TestMenuDO testMenuDO = TestMenuDO.builder().name("测试菜单-0").build();
        TestChildDO childDO = TestChildDO.builder().name("Test-Child-4").build();
        TestMenuDO testMenuDO1 = TestMenuDO.builder().name("测试菜单-4").childDO(childDO).build();
//        testMenuRepository.save(testMenuDO);
//        testMenuRepository.save(testMenuDO1);
//        TestRoleDO roleDO = TestRoleDO.builder().auth(0).roleName("测试角色-0").menuDO(testMenuDO).build();
        TestRoleDO roleDO1 = TestRoleDO.builder().auth(4).roleName("测试角色-4").menuDO(testMenuDO1).build();
//        testRoleRepository.save(roleDO);
        testRoleRepository.save(roleDO1);
        System.out.println("-------");
    }
}
