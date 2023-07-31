package com.lz.redis.demo.controller;

import com.lz.redis.demo.statemachine.Order;
import com.lz.redis.demo.statemachine.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
/**
 * @author : liuze
 * @date: 2023/7/11 13:24
 **/
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("/create")
    @ResponseBody
    public Map<String,Object> create(){
        HashMap<String, Object> map = new HashMap<>();
        Order order1 = orderService.create();
        map.put("status",200);
        map.put("data",order1);
        return map;
    }

    @GetMapping("/pay/{id}")
    @ResponseBody
    public Map<String,Object> pay(@PathVariable int id){
        HashMap<String, Object> map = new HashMap<>();
        Order order1 = orderService.pay(id);
        map.put("status",200);
        map.put("data",order1);
        return map;
    }
    @GetMapping("/deliver/{id}")
    @ResponseBody
    public Map<String,Object> deliver(@PathVariable int id){
        HashMap<String, Object> map = new HashMap<>();
        Order order1 = orderService.deliver(id);
        map.put("status",200);
        map.put("data",order1);
        return map;
    }
    @GetMapping("/list")
    @ResponseBody
    public Map<String,Object> list(){
        HashMap<String, Object> map = new HashMap<>();
        Map<Integer,Order> orders = orderService.getOrders();
        map.put("status",200);
        map.put("data",orders);
        return map;
    }
}
