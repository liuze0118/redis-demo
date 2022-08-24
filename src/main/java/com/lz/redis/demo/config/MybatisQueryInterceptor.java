package com.lz.redis.demo.config;

import com.baomidou.mybatisplus.annotation.TableField;
import com.lz.redis.demo.utils.SM4Utils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.Map;
import java.util.Properties;

/**
 * @author : liuze
 * @date: 2022/8/17 11:25
 **/
@Component
@Intercepts({
        @Signature(type = Executor.class,method = "query",args = {MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class})
       // ,@Signature(type = ParameterHandler.class,method = "setParameters",args = {PreparedStatement.class})
})
public class MybatisQueryInterceptor implements Interceptor {
    /**
     * 使用 @Param("crypt")注解的参数（只支持自定义对象 且同时添加CryptEntity和CryptField注解）
     */
    public static final String paramVal = "crypt";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //获取查询参数，查询条件是否需要加密
        Object[] args = invocation.getArgs();
        Object parameter = args[1];
        Object result = null;
        //设置执行标识
        boolean flag = true;
        if (parameter instanceof MapperMethod.ParamMap) {
            Map paramMap = (Map) parameter;

        }else{
            ReflectionUtils.doWithFields(parameter.getClass(),field -> {
                ReflectionUtils.makeAccessible(field);
                // 判断是否有@AutoStuff注解
                if(field.isAnnotationPresent(TableField.class)){
                    TableField tableField = field.getAnnotation(TableField.class);
                    // addFlag是否为true
                    if(tableField != null && tableField.typeHandler() == EncTypeHandler.class){
                        // addMethod为空则抛出异常
                        System.out.println(field.get(parameter)+"");
                        field.set(parameter, SM4Utils.encryptSm4( field.get(parameter)+""));
                    }
                }
            });

        }
        return invocation.proceed();
    }

    @Override
    public void setProperties(Properties properties) {
        System.out.println(properties);
    }
}
