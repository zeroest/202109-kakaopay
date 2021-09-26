package me.zeroest.kyd_kakaopay.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zeroest.kyd_kakaopay.config.rabbitmq.RabbitConfig;
import me.zeroest.kyd_kakaopay.domain.invest.log.InvestResult;
import me.zeroest.kyd_kakaopay.domain.invest.log.ProductInvestLog;
import me.zeroest.kyd_kakaopay.domain.product.Product;
import me.zeroest.kyd_kakaopay.domain.product.status.ProductInvestStatus;
import me.zeroest.kyd_kakaopay.exception.BaseCustomException;
import me.zeroest.kyd_kakaopay.exception.ExceptionCode;
import me.zeroest.kyd_kakaopay.repository.invest.status.ProductInvestStatusRepository;
import me.zeroest.kyd_kakaopay.repository.invest.log.ProductInvestLogRepository;
import me.zeroest.kyd_kakaopay.service.rabbitmq.SendRabbitService;
import me.zeroest.kyd_kakaopay.service.redisson.RedissonService;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
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

    private final SendRabbitService sendRabbitService;

    private final ProductInvestStatusRepository productInvestStatusRepository;
    private final ProductInvestLogRepository productInvestLogRepository;

    @Transactional
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
        AtomicLong lastAccureUserInvest = new AtomicLong(0L);
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

            lastAccureUserInvest.set(productInvestLogRepository.lastAccrueUserInvest(userId, productId));

            redissonClient.getSet(investStatus.getRedisInvestingCntKey()).add(userId);

        });
        log.info("Success request invest validation");

        // Save invest log
        final ProductInvestLog investLog = ProductInvestLog.builder()
                .userId(userId)
                .investAmount(investAmount)
                .product(product)
                .investResult(InvestResult.FAIL)
                .failReason(InvestResult.MESSAGE_PENDING)
                .accrueUserInvest(lastAccureUserInvest.get() + investAmount)
                .accrueProductInvest(resultAmount.get())
                .build();
        final ProductInvestLog investLogResult = productInvestLogRepository.save(investLog);

        // Send message to invest
        sendRabbitService.sendMessage(
                RabbitConfig.REQUEST_INVEST_EXCHANGE,
                RabbitConfig.makeInvestSuccessMessage(productId, investLogResult.getId())
        );

        return resultAmount.get();

    }

}
