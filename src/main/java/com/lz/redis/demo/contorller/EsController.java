package com.lz.redis.demo.contorller;

import com.lz.redis.demo.repository.EsGoodRepository;
import com.lz.redis.demo.repository.GoodRepository;
import com.lz.redis.demo.vo.Good;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/es")
@Slf4j
public class EsController {
    @Autowired
    private RestHighLevelClient highLevelClient;

    @Autowired
    private GoodRepository goodRepository;
    @Autowired
    private EsGoodRepository esGoodRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;


    @GetMapping("/index/{index}")
    public String esIndex(@PathVariable String index){
        String returnStr = null;
        Map<String,String> sourceMap = new HashMap<>();
        sourceMap.put("feature","high-level-rest-client");
        IndexRequest request = new IndexRequest("test_index")
                .id(UUID.randomUUID().toString())
                .source(sourceMap)
                .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        try {
            IndexResponse response  = highLevelClient.index(request, RequestOptions.DEFAULT);
            returnStr = response.getIndex();
        } catch (IOException e) {
            log.error("查询es索引异常",e);
        }
        return returnStr;
    }
    @GetMapping("/create/{index}")
    public String createIndex(@PathVariable String index){
        String returnStr = null;
        Iterable<Good> save = elasticsearchOperations.save(createEsDoc2());
        return returnStr;
    }

    @GetMapping("/save/{index}")
    public String getIndex(@PathVariable String index){
        goodRepository.save(new Good(7L, "Sansum", 5400, "http://aliyun.com", new Date(), 7L, "手机", "三星", 566));
        return null;
    }

    @GetMapping("/get/{name}")
    public String getDocByName(@PathVariable String name){
        List<Good> goodsByName = goodRepository.findGoodsByName(name);
        return null;
    }

    @GetMapping("/getNB/{name}/{brandName}")
    public String getDocByNameOrBrandName(@PathVariable String name,@PathVariable String brandName){
        List<SearchHit<Good>> searchHits = goodRepository.findGoodsByNameOrBrandName(name, brandName);
        return null;
    }

    @GetMapping("/getB/{brandName}/{num}/{size}")
    public String getDocByBrandName(@PathVariable String brandName,@PathVariable int num, @PathVariable int size){
        Page<Good> goodsByName = esGoodRepository.findByBrandName(brandName,PageRequest.of(num,size).withSort(Sort.Direction.DESC,"spuId"));
        return null;
    }



    List<Good> createEsDoc(){
        Good good = new Good(1L, "HonorMate5Pro", 5400, "http://aliyun.com", new Date(), 1L, "手机", "华为", 566);
        Good good1 = new Good(2L, "VivoX70", 4500, "http://aliyun.com", new Date(), 2L, "手机", "VIVO", 999);
        Good good2 = new Good(3L, "OPOPRENO", 4000, "http://aliyun.com", new Date(), 3L, "手机", "OPPO", 1080);
        Good good3 = new Good(4L, "Mi9S", 3600, "http://aliyun.com", new Date(), 4L, "手机", "小米", 1770);
        Good good4 = new Good(5L, "Iphone12Plus", 8500, "http://aliyun.com", new Date(), 5L, "手机", "苹果", 800);
        Good good5 = new Good(6L, "Mz98", 6000, "http://aliyun.com", new Date(), 6L, "手机", "魅族", 466);
        return Arrays.asList(good,good1,good2,good3,good4,good5);
    }
    List<Good> createEsDoc2(){
        Good good1 = new Good(8L, "VivoX60", 4500, "http://aliyun.com", new Date(), 2L, "手机", "VIVO", 999);
        Good good2 = new Good(9L, "VivoX50", 4500, "http://aliyun.com", new Date(), 2L, "手机", "VIVO", 999);
        Good good3 = new Good(10L, "VivoX40", 4500, "http://aliyun.com", new Date(), 2L, "手机", "VIVO", 999);
        Good good4 = new Good(11L, "VivoX30", 4500, "http://aliyun.com", new Date(), 2L, "手机", "VIVO", 999);
        return Arrays.asList(good1,good2,good3,good4);
    }
}
