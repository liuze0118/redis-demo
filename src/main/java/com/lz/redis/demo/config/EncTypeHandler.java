package com.lz.redis.demo.config;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.lz.redis.demo.utils.SM4Utils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author : liuze
 * @date: 2022/8/15 17:12
 **/
@MappedTypes(String.class)
@MappedJdbcTypes({JdbcType.VARCHAR})
public class EncTypeHandler extends BaseTypeHandler<String> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String s, JdbcType jdbcType) throws SQLException {
        ps.setString(i,this.encString(s));
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String result = rs.getString(columnName);
        return StringUtils.isBlank(result) ? null : this.decString(result);
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String result = rs.getString(columnIndex);
        return StringUtils.isBlank(result) ? null : this.decString(result);
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String result = cs.getString(columnIndex);
        return StringUtils.isBlank(result) ? null : this.decString(result);
    }

    protected String decString(String str){
        return SM4Utils.decryptSm4(str);
    }

    protected String encString(String str){
        return SM4Utils.encryptSm4(str);
    }

}
