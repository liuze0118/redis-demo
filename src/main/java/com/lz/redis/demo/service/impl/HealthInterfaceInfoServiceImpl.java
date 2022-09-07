package com.lz.redis.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.redis.demo.dao.HealthInterfaceInfoDao;
import com.lz.redis.demo.model.entity.mysql.HealthInterfaceInfo;
import com.lz.redis.demo.service.HealthInterfaceInfoService;
import org.springframework.stereotype.Service;

/**
 * @author : liuze
 * @date: 2022/8/25 14:59
 **/
@Service
public class HealthInterfaceInfoServiceImpl extends ServiceImpl<HealthInterfaceInfoDao,HealthInterfaceInfo> implements HealthInterfaceInfoService {
}
