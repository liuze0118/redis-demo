package com.lz.redis.demo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lz.redis.demo.vo.Good;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
@Slf4j
public class StreamOperate {

    public static void allMatch(){
        List<Good> goodList = new ArrayList<>();
        for (int i = 1; i < 1000000; i++) {
            Good good = new Good();
            good.setId(Long.parseLong(i+""));
            good.setPrice(1);
            goodList.add(good);
        }
        long start = System.currentTimeMillis();
        boolean b = goodList.parallelStream().allMatch(good -> good.getPrice() == 1);
        System.out.println(b + "  allMatch用时:"+(System.currentTimeMillis()-start));
        long start1 = System.currentTimeMillis();
        boolean b1 = goodList.parallelStream().filter(good -> good.getPrice() == 0).count() == 0;
        System.out.println(b1 + "  count用时:"+(System.currentTimeMillis()-start1));
    }

    public static void toMapFunction(){
        List<Good> goodList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Good good = new Good();
            good.setId(Long.valueOf(i+""));
            good.setName("test_"+i);
            goodList.add(good);
        }
        Good good2 = new Good();
        String jsonString = JSON.toJSONString(goodList);
        good2.setId(1L);
        good2.setName("1231");
        goodList.add(good2);
        //Map<Long, Good> collect1 = goodList.stream().collect(Collectors.toMap(Good::getId, g -> g));
        Map<Long, Good> collect = goodList.stream().collect(Collectors.toMap(Good::getId, g -> g, (k1, k2) -> {return k1;}));
        System.out.println(collect);
    }

    public static void createList(){
        ArrayList<Good> goods = new ArrayList<>(0);
        for (int i = 0; i < 3; i++) {
            Good good = new Good();
            good.setId(Long.valueOf(i+""));
            goods.add(good);
        }
        List<Good> collect = goods.stream().sorted(Comparator.comparing(Good::getId).reversed()).collect(Collectors.toList());
    }

    public static void userOptional() throws Exception {
        Good good = null;
        Optional<Good> optionalGood = Optional.ofNullable(good);
        String name = optionalGood.orElseThrow(() -> new Exception("商品不能为空")).getName();
        Good good1 = optionalGood.orElse(new Good());
        Good good21 = optionalGood.orElseGet(() -> {
            Good good2 = new Good();
            good2.setName("good2");
            return good2;
        });
    }

    public static void removeIf(){
        List<Good> goodList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Good good = new Good();
            good.setId(Long.parseLong(i+""));
            goodList.add(good);
        }
        goodList.removeIf(good -> good.getId() <5L);
        System.out.println(goodList.size());
    }

    public static void testJSON(){
        String jstr = "{\n" +
                "  \"errcode\": 0,\n" +
                "  \"errmsg\": \"成功\",\n" +
                "  \"data\": {\n" +
                "    \"goodsClass\": [\n" +
                "      {\n" +
                "        \"id\": 1232,\n" +
                "        \"name\": \"ba\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"requestId\": \"\",\n" +
                "  \"requestMsg\": \"\"\n" +
                "}";

        JSONObject jsonObject = JSONObject.parseObject(jstr);
        JSONObject data = (JSONObject) jsonObject.get("data");
        if(null != data){
            JSONArray goodsClass = (JSONArray) data.get("goodsClass");
            if(null != goodsClass){
                List<String> ids = goodsClass.stream().map(temp -> (((JSONObject) temp).get("id")+"").trim()).collect(Collectors.toList());
                if(StringUtils.isEmpty(ids)){
                    System.out.println("====");
                }
            }
        }
    }

    public static void main(String[] args) {
//        testJSON();
//        removeIf();
//        allMatch();
        toMapFunction();
//        createList();
//        try {
//            userOptional();
//        } catch (Exception e) {
//            log.error("查询商品异常",e);
//        }
//        Long id = null;
//        Assert.notNull(id,"id不能为空");
//        System.out.println();
    }
}
