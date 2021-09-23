package me.zeroest.kyd_kakaopay.service;

import me.zeroest.kyd_kakaopay.domain.product.Product;
import me.zeroest.kyd_kakaopay.domain.product.status.ProductInvestStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRedisServiceTest {

    @Autowired
    ProductRedisService productRedisService;
    @Autowired
    RedisTemplate redisTemplate;

    private long productId = -1L;
    private long expectedInvestingCnt = 3L;
    private long expectedInvestedAmount = 5L;

    @BeforeEach()
    void beforeEach() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(ProductInvestStatus.REDIS_INVESTING_CNT + productId, expectedInvestingCnt + "");
        valueOperations.set(ProductInvestStatus.REDIS_INVESTED_AMOUNT_PREFIX + productId, expectedInvestedAmount + "");
    }

    @AfterEach
    void afterEach() {
        redisTemplate.delete(ProductInvestStatus.REDIS_INVESTING_CNT + productId);
        redisTemplate.delete(ProductInvestStatus.REDIS_INVESTED_AMOUNT_PREFIX + productId);
    }


    @DisplayName("productId 를 통해 총 투자한 금액을 반환받는다.")
    @Test
    void getInvestedAmountById() {

        long investedAmount = productRedisService.getInvestedAmount(productId);

        assertEquals(expectedInvestedAmount, investedAmount);

    }

    @DisplayName("productId 가 redis에 존재하지 않을시 총 투자금액은 0을 반환한다.")
    @Test
    void getInvestedAmountByEmpty() {

        long investedAmount = productRedisService.getInvestedAmount(-100L);

        assertEquals(0L, investedAmount);

    }

    @DisplayName("Product 엔티티를 통해 총 투자한 금액을 반환받는다.")
    @Test
    void getInvestedAmountByProduct() {

        Product product = Product.builder()
                .id(productId)
                .build();

        long investedAmount = productRedisService.getInvestedAmount(product);

        assertEquals(expectedInvestedAmount, investedAmount);

    }


    @DisplayName("productId 를 통해 투자한 사람의 수를 반환받는다.")
    @Test
    void getInvestingCntById() {

        long investingCnt = productRedisService.getInvestingCnt(productId);

        assertEquals(expectedInvestingCnt, investingCnt);

    }

    @DisplayName("productId 가 redis에 존재하지 않을시 투자한 사람의 수는 0을 반환한다.")
    @Test
    void getInvestingCntByEmpty() {

        long investingCnt = productRedisService.getInvestingCnt(-100L);

        assertEquals(0L, investingCnt);

    }

    @DisplayName("Product 엔티티를 통해 투자한 사람의 수를 반환받는다.")
    @Test
    void getInvestingCntByProduct() {

        Product product = Product.builder()
                .id(productId)
                .build();

        long investingCnt = productRedisService.getInvestingCnt(product);

        assertEquals(expectedInvestingCnt, investingCnt);

    }

}