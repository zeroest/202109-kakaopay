package me.zeroest.kyd_kakaopay.service;

import lombok.RequiredArgsConstructor;
import me.zeroest.kyd_kakaopay.domain.product.Product;
import me.zeroest.kyd_kakaopay.domain.product.status.ProductInvestStatus;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductRedisService {

    private final RedisTemplate redisTemplate;

    public long getInvestedAmount(long productId) {
        ValueOperations valueOps = redisTemplate.opsForValue();

        try{
            return Long.parseLong(valueOps.get(ProductInvestStatus.REDIS_INVESTED_AMOUNT_PREFIX + productId).toString());
        }catch (NullPointerException npe){
            return 0L;
        }
    }
    public long getInvestedAmount(Product product) {
        if (Objects.isNull(product)) {
            return 0L;
        }
        return getInvestedAmount(product.getId());
    }

    public long getInvestingCnt(long productId) {
        ValueOperations valueOps = redisTemplate.opsForValue();

        try {
            return Long.parseLong(valueOps.get(ProductInvestStatus.REDIS_INVESTING_CNT + productId).toString());
        }catch (NullPointerException npe){
            return 0L;
        }
    }
    public long getInvestingCnt(Product product) {
        if (Objects.isNull(product)) {
            return 0L;
        }
        return getInvestingCnt(product.getId());
    }

}
