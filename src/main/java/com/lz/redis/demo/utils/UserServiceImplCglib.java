package com.lz.redis.demo.utils;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class UserServiceImplCglib implements MethodInterceptor {
    public <T> T getInstance(Object target, Class<T> clazz) {
        //字节码加强器：用来创建动态代理类
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass()); //代理的目标对象
        enhancer.setCallback(this); //回调类，在代理类方法调用时会回调Callback类的intercept方法

        Object result = enhancer.create(); //创建代理类
        System.out.println(result.getClass().getName());
        return (T)result;
    }
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        //对目标方法进行拦截处理
        System.out.println("start...");
        System.out.println("xxljob-------"+method.isAnnotationPresent(XxlJob.class));
        Object result = proxy.invokeSuper(obj, args); //调用目标类（父类）的方法
        System.out.println("end");
        return result;
    }

}
