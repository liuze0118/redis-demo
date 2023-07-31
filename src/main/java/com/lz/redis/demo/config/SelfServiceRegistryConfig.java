package com.lz.redis.demo.config;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.cloud.client.serviceregistry.Registration;
//import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import javax.annotation.Resource;

@Configuration
@ConditionalOnProperty(prefix = "management.endpoint.self-registry",name ="enable", havingValue = "true")
public class SelfServiceRegistryConfig {

//    @Resource
//    private Registration registration;
//
//    @Bean
//    public SelfServiceRegistryEndpoint SelfServiceRegistryEndpoint(ServiceRegistry serviceRegistry) {
//        SelfServiceRegistryEndpoint endpoint = new SelfServiceRegistryEndpoint(serviceRegistry);
//        endpoint.setRegistration(this.registration);
//        return endpoint;
//    }
//
//    @Endpoint(id="self-registry")
//    public class SelfServiceRegistryEndpoint{
//        private static final String STATUS_UP = "UP";
//        private static final String STATUS_DOWN = "DOWN";
//
//        private final ServiceRegistry serviceRegistry;
//        private Registration registration;
//
//        public SelfServiceRegistryEndpoint(ServiceRegistry serviceRegistry) {
//            this.serviceRegistry = serviceRegistry;
//        }
//        public void setRegistration(Registration registration) {
//            this.registration = registration;
//        }
//
//        /**
//         * 调用方式：curl -X "POST" "http://${ip}:${port}/{context-path}/actuator/self-registry?status=DOWN"
//         * -H "Content-Type: application/vnd.spring-boot.actuator.v2+json;charset=UTF-8"
//         * @Author: liuze
//         * @Date: 2022/6/30
//         **/
//        @WriteOperation
//        public ResponseEntity<?> setStatus(String status) {
//            Assert.notNull(status, "status may not by null");
//            if (this.registration == null) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no registration found");
//            } else {
//                if(STATUS_DOWN.equals(status)){
//                    serviceRegistry.deregister(registration);
//                }else if(STATUS_UP.equals(status)){
//                    serviceRegistry.register(registration);
//                }else {
//                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no matching status");
//                }
//                return ResponseEntity.ok().body("OK");
//            }
//        }
//
//        @ReadOperation
//        public ResponseEntity getStatus() {
//            return this.registration == null ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("no registration found") : ResponseEntity.ok().body(this.serviceRegistry.getStatus(this.registration));
//        }

//    }

}
