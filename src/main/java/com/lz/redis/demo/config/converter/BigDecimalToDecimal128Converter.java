package com.lz.redis.demo.config.converter;

import com.mongodb.lang.NonNull;
import org.bson.types.Decimal128;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.math.BigDecimal;

/**
 * @description: BigDecimal转换Decimal128
 * @author: liuze
 */
@WritingConverter
public class BigDecimalToDecimal128Converter implements Converter<BigDecimal, Decimal128> {

    @Override
    public Decimal128 convert(@NonNull BigDecimal source) {
        return new Decimal128(source);
    }
}
