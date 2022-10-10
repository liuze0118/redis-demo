package com.lz.redis.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lz.redis.demo.dao.MatchResultDao;
import com.lz.redis.demo.model.dto.APIMatchFoodV2RequestDTO;
import com.lz.redis.demo.model.dto.ThirdPartyFoodMatchCondition;
import com.lz.redis.demo.model.dto.ThirdPartyFoodMatchRequestDTO;
import com.lz.redis.demo.model.dto.ThirdPartyFoodMatchResponseDTO;
import com.lz.redis.demo.model.entity.mysql.MatchResult;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @author : liuze
 * @date: 2022/9/22 14:22
 **/
@Service
public class MatchResultServiceImpl {
    @Autowired
    private MatchResultDao resultDao;

    private RestTemplate restTemplate = new RestTemplate();

    public void testMatch(String batchNo){
        ThirdPartyFoodMatchRequestDTO requestDTO = new ThirdPartyFoodMatchRequestDTO();
        requestDTO.setUserId("1ds222qq");
        requestDTO.setAppId("61b1b77787a7ee68c2a04c99");
        requestDTO.setRecipesDate("2022-05-31");
        ThirdPartyFoodMatchCondition condition = new ThirdPartyFoodMatchCondition();
        condition.setGender("1");
        condition.setReMatch(true);
        requestDTO.setFoodMatchConditions(condition);
        List<MatchResult> resultList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            String height = (160 + new Random().nextInt(20)) + "";
            String weight = (55 + new Random().nextInt(10)) + "";
            int finalEnergy = new Random().nextInt(100) +1200 + 100*((i%19));
            String management = (i%4+1)+"";
            requestDTO.getFoodMatchConditions().setEnergy(finalEnergy);
            requestDTO.getFoodMatchConditions().setHeight(height);
            requestDTO.getFoodMatchConditions().setWeight(weight);
            requestDTO.getFoodMatchConditions().setManagement(management);
            int mg = (i%4+1);
            if(mg == 1){
                handleMealList(requestDTO, i);
            }else if(mg == 2){
                handleMealList(requestDTO, i);
            }else if(mg == 3){
                handleMealList(requestDTO, i);
            }else {
                handleMealList(requestDTO, i);
            }
            JSONObject result = null;
            try {
                result = restTemplate.postForObject("http://localhost:9010/meal/v3/api/recipes/detail?bizType=111&configId=020501", requestDTO, JSONObject.class);
            } catch (RestClientException e) {
                System.out.println("err---------"+i);
                continue;
            }
            MatchResult matchResult = MatchResult.builder().batchNo(batchNo + "/" + i).energy(finalEnergy).management(management).
                    weight(weight).height(height).spendTime(result.getString("spendTime")).build();;
            JSONObject data = result.getJSONObject("data");
            if(data != null){
                String b = data.getJSONArray("breakfast").getJSONObject(0).getString("foodName");
                String l = data.getJSONArray("lunch").getJSONObject(0).getString("foodName");
                String d = data.getJSONArray("dinner").getJSONObject(0).getString("foodName");
                matchResult.setMealKey(data.getString("mealKey"));
                matchResult.setBreakFastName(b);
                matchResult.setLunName(l);
                matchResult.setSuperName(d);
                matchResult.setStatus(1);
            }else {
                matchResult.setStatus(0);
            }
            resultList.add(matchResult);
        }
        resultList.stream().forEach(r->{
            resultDao.insert(r);
        });
    }

    public void testMatchBase(String batchNo){
        APIMatchFoodV2RequestDTO requestDTO = new APIMatchFoodV2RequestDTO();
        requestDTO.setUserId("1ds222qq");
        requestDTO.setAppId("61b1b77787a7ee68c2a04c99");
        requestDTO.setBizType("11111");
        requestDTO.setBirthday("1991-05-31");
        requestDTO.setGender("2");
        requestDTO.setPlanningDate("2022-09-31");
        requestDTO.setActivityLevel("1");
        requestDTO.setExerciseRiskLevel("1");
        requestDTO.setReMatch("true");
        List<MatchResult> resultList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            String height = (160 + new Random().nextInt(20)) + "";
            String weight = (55 + new Random().nextInt(30)) + "";
            int finalEnergy = new Random().nextInt(100) +1200 + 100*((i%23));
            String management = (i%4+1)+"";
            //requestDTO.setRecommendedIntakeEnergy(finalEnergy);
            requestDTO.setHeight(height);
            requestDTO.setWeight(weight);
            HttpHeaders headers = new HttpHeaders();
            headers.set("token", "eyJhbGciOiJIUzI1NiJ9.eyJhcHBJZCI6IjViMzM5NDFiODQyNzRhMGFhNDgyZTEwNSIsImlhdCI6MTY2Mzg1NDU0MSwiZXhwIjoxNjY3NDU0NTQxfQ.qufRMR3M4DOAk8sArW1gCQtl9zfuKVXr7vWfIuKLLnE");
            headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<APIMatchFoodV2RequestDTO> entity = new HttpEntity<>(requestDTO, headers);
            JSONObject result = null;
            try {
                result = restTemplate.postForObject("http://localhost:9010/v4/api/food-match/match?bizType=111111&configId=020501", entity, JSONObject.class);
                MatchResult matchResult = MatchResult.builder().batchNo(batchNo + "/" + i).management(management).
                        weight(weight).height(height).spendTime(result.getString("spendTime")).build();;
                JSONObject data = result.getJSONObject("data");
                if(data != null){
                    String b = data.getJSONObject("breakfastRes").getJSONArray("foods").getJSONObject(0).getString("foodName");
                    String l = data.getJSONObject("lunchRes").getJSONArray("foods").getJSONObject(0).getString("foodName");
                    String d = data.getJSONObject("supperRes").getJSONArray("foods").getJSONObject(0).getString("foodName");
                    JSONArray scheme = data.getJSONArray("scheme");
                    matchResult.setMealKey(scheme != null?scheme.toJSONString():null);
                    matchResult.setBreakFastName(b);
                    matchResult.setLunName(l);
                    matchResult.setSuperName(d);
                    matchResult.setStatus(1);
                    matchResult.setEnergy(data.getIntValue("energyAdvise"));
                }else {
                    matchResult.setStatus(0);
                }
                resultList.add(matchResult);
                System.out.println("==================  " + i + " ===========");
            } catch (RestClientException e) {
                System.out.println("err---------"+i);
            }
        }
        resultList.stream().forEach(r->{
            resultDao.insert(r);
        });
    }

    private void handleMealList(ThirdPartyFoodMatchRequestDTO requestDTO, int i) {
        if(i%2==0){
            requestDTO.getFoodMatchConditions().setExtraMealList(Arrays.asList("1","3","5"));
        }else{
            requestDTO.getFoodMatchConditions().setExtraMealList(Arrays.asList("1","2","3","4","5","6"));
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 30; i++) {
            System.out.println();
        }
    }

}
