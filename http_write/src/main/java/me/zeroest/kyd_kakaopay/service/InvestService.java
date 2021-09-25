package me.zeroest.kyd_kakaopay.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zeroest.kyd_kakaopay.config.RabbitConfig;
import me.zeroest.kyd_kakaopay.domain.product.Product;
import me.zeroest.kyd_kakaopay.domain.product.status.ProductInvestStatus;
import me.zeroest.kyd_kakaopay.exception.BaseCustomException;
import me.zeroest.kyd_kakaopay.exception.ExceptionCode;
import me.zeroest.kyd_kakaopay.repository.ProductInvestStatusRepository;
import me.zeroest.kyd_kakaopay.service.redisson.RedissonService;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvestService {

    private final RedissonClient redissonClient;
    private final RedissonService redissonService;

    private final RabbitTemplate rabbitTemplate;

    private final ProductInvestStatusRepository productInvestStatusRepository;

    @Transactional(readOnly = true)
    public long invest(String userId, Long productId, Long investAmount) {

        // exist product
        ProductInvestStatus investStatus = productInvestStatusRepository.findByProductId(productId)
                .orElseThrow(() -> new BaseCustomException(ExceptionCode.NOT_EXIST_PRODUCT));
        Product product = investStatus.getProduct();
        if (Objects.isNull(product)) {
            throw new BaseCustomException(ExceptionCode.NOT_EXIST_PRODUCT);
        }

        // ONGOING product
        if(investStatus.isCompleted()){
            throw new BaseCustomException(ExceptionCode.SOLD_OUT);
        }

        AtomicLong resultAmount = new AtomicLong();
        redissonService.locking(investStatus.getRedisLockKeyByProductId(), () -> {

            final RAtomicLong investedAmount = redissonClient.getAtomicLong(investStatus.getRedisInvestedAmountKey());

            // sold out
            if (product.getTotalAmount() <= investedAmount.get()){
                throw new BaseCustomException(ExceptionCode.SOLD_OUT);
            }

            // invest amount overflow
            if(product.getTotalAmount() < (investedAmount.get() + investAmount)){
                throw new BaseCustomException(ExceptionCode.INVEST_AMOUNT_OVERFLOW);
            }

            resultAmount.set(investedAmount.addAndGet(investAmount));

        });


        // Send message to invest
        rabbitTemplate.convertAndSend(RabbitConfig.requestInvestExchange, "", userId + ":" + productId + ":" + investAmount);

        return resultAmount.get();

    }

}
