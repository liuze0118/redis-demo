package com.lz.redis.demo.statemachine;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author : liuze
 * @date: 2023/7/11 11:52
 **/
@Getter
@AllArgsConstructor
public enum OrderStatus {
    WAIT_PAY(1,"待支付"),
    WAIT_DELIVER(2,"待发货"),
    WAIT_RECEIVE(3,"待收货"),
    FINISH(4,"已完成");

    private final int status;
    private final String desc;
}
