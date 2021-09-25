package me.zeroest.kyd_kakaopay.service;

import me.zeroest.kyd_kakaopay.domain.product.Product;
import me.zeroest.kyd_kakaopay.domain.product.status.InvestStatus;
import me.zeroest.kyd_kakaopay.domain.product.status.ProductInvestStatus;
import me.zeroest.kyd_kakaopay.exception.BaseCustomException;
import me.zeroest.kyd_kakaopay.exception.ExceptionCode;
import me.zeroest.kyd_kakaopay.repository.ProductInvestStatusRepository;
import me.zeroest.kyd_kakaopay.service.redisson.LockingWork;
import me.zeroest.kyd_kakaopay.service.redisson.RedissonService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvestServiceTest {

    @Mock
    private ProductInvestStatusRepository productInvestStatusRepository;
    @Mock
    private RedissonClient redissonClient;
    @Mock
    private RedissonService redissonService;

    @InjectMocks
    private InvestService investService;

    private String userId = "userId";

    @Mock
    RAtomicLong investedAmount;


    @DisplayName("invest - 투자 성공시 redis 에 investAmount 값이 추가된다.")
    @Test
    void investAddRedis() {

        long totalAmount = 100L;
        long areadyInvestAmount = 90L;
        long investAmount = 10L;

        when(productInvestStatusRepository.findByProductId(any(Long.class)))
                .thenReturn(Optional.of(
                        ProductInvestStatus.builder()
                                .product(
                                        Product.builder()
                                                .totalAmount(totalAmount)
                                                .build()
                                )
                                .investStatus(InvestStatus.ONGOING)
                                .build()
                ));

        when(investedAmount.get())
                .thenReturn(areadyInvestAmount);
        when(investedAmount.addAndGet(any(Long.class)))
                .thenReturn(areadyInvestAmount+investAmount);
        when(redissonClient.getAtomicLong(anyString()))
                .thenReturn(investedAmount);
        when(redissonService.locking(anyString(), any(LockingWork.class)))
                .thenAnswer((invocation -> {
                    final LockingWork callback = invocation.getArgument(1);
                    callback.invoke();
                    return null;
                }));

        Long resultAmount = assertDoesNotThrow(() -> investService.invest(userId, 1L, investAmount));

        assertEquals(areadyInvestAmount+investAmount, resultAmount);

    }



    @DisplayName("invest - InvestStatus 가 존재하지 않으면 NOT_EXIST_PRODUCT 반환")
    @Test
    void investInvestStatus() {

        final ExceptionCode expected = ExceptionCode.NOT_EXIST_PRODUCT;

        when(productInvestStatusRepository.findByProductId(any(Long.class)))
                .thenReturn(Optional.empty());

        BaseCustomException notExistProduct = assertThrows(
                BaseCustomException.class,
                () -> investService.invest(userId, 1L, 10L)
        );

        assertEquals(notExistProduct.getCode(), expected.getCode());
        assertEquals(notExistProduct.getMessage(), expected.getMessage());

    }

    @DisplayName("invest - Product 가 존재하지 않으면 NOT_EXIST_PRODUCT 반환")
    @Test
    void investProduct() {

        final ExceptionCode expected = ExceptionCode.NOT_EXIST_PRODUCT;

        when(productInvestStatusRepository.findByProductId(any(Long.class)))
                .thenReturn(Optional.of(
                        ProductInvestStatus.builder()
                                .product(null)
                                .build()
                ));

        BaseCustomException notExistProduct = assertThrows(
                BaseCustomException.class,
                () -> investService.invest(userId, 1L, 10L)
        );

        assertEquals(notExistProduct.getCode(), expected.getCode());
        assertEquals(notExistProduct.getMessage(), expected.getMessage());

    }

    @DisplayName("invest - InvestStatus 의 investStatus 가 COMPLETE 일 때 SOLD_OUT 반환")
    @Test
    void investInvestStatusComplete() {

        final ExceptionCode expected = ExceptionCode.SOLD_OUT;

        when(productInvestStatusRepository.findByProductId(any(Long.class)))
                .thenReturn(Optional.of(
                        ProductInvestStatus.builder()
                                .product(
                                        Product.builder().build()
                                )
                                .investStatus(InvestStatus.COMPLETE)
                                .build()
                ));

        BaseCustomException notExistProduct = assertThrows(
                BaseCustomException.class,
                () -> investService.invest(userId, 1L, 10L)
        );

        assertEquals(notExistProduct.getCode(), expected.getCode());
        assertEquals(notExistProduct.getMessage(), expected.getMessage());

    }

    @DisplayName("invest - investedAmount 가 totalAmount 보다 같거나 높을 때 SOLD_OUT 반환")
    @Test
    void investSoldOut() {

        final ExceptionCode expected = ExceptionCode.SOLD_OUT;

        when(productInvestStatusRepository.findByProductId(any(Long.class)))
                .thenReturn(Optional.of(
                        ProductInvestStatus.builder()
                                .product(
                                        Product.builder()
                                                .totalAmount(100L)
                                                .build()
                                )
                                .investStatus(InvestStatus.ONGOING)
                                .build()
                ));

        when(investedAmount.get())
                .thenReturn(100L);
        when(redissonClient.getAtomicLong(anyString()))
                .thenReturn(investedAmount);
        when(redissonService.locking(anyString(), any(LockingWork.class)))
                .thenAnswer((invocation -> {
                    final LockingWork callback = invocation.getArgument(1);
                    callback.invoke();
                    return null;
                }));

        BaseCustomException notExistProduct = assertThrows(
                BaseCustomException.class,
                () -> investService.invest(userId, 1L, 10L)
        );

        assertEquals(notExistProduct.getCode(), expected.getCode());
        assertEquals(notExistProduct.getMessage(), expected.getMessage());

    }

    @DisplayName("invest - investedAmount + investAmount 가 totalAmount 보다 높을 때 INVEST_AMOUNT_OVERFLOW 반환")
    @Test
    void investInvestAmountOverflow() {

        final ExceptionCode expected = ExceptionCode.INVEST_AMOUNT_OVERFLOW;

        when(productInvestStatusRepository.findByProductId(any(Long.class)))
                .thenReturn(Optional.of(
                        ProductInvestStatus.builder()
                                .product(
                                        Product.builder()
                                                .totalAmount(100L)
                                                .build()
                                )
                                .investStatus(InvestStatus.ONGOING)
                                .build()
                ));

        when(investedAmount.get())
                .thenReturn(90L);
        when(redissonClient.getAtomicLong(anyString()))
                .thenReturn(investedAmount);
        when(redissonService.locking(anyString(), any(LockingWork.class)))
                .thenAnswer((invocation -> {
                    final LockingWork callback = invocation.getArgument(1);
                    callback.invoke();
                    return null;
                }));

        BaseCustomException notExistProduct = assertThrows(
                BaseCustomException.class,
                () -> investService.invest(userId, 1L, 11L)
        );

        assertEquals(notExistProduct.getCode(), expected.getCode());
        assertEquals(notExistProduct.getMessage(), expected.getMessage());

    }

}