package com.lz.redis.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lz.redis.demo.model.entity.mysql.MatchResult;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface MatchResultDao extends BaseMapper<MatchResult> {

}
