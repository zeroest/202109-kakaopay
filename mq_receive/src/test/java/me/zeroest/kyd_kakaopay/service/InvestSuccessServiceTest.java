package me.zeroest.kyd_kakaopay.service;

import me.zeroest.kyd_kakaopay.config.rabbitmq.RabbitConfig;
import me.zeroest.kyd_kakaopay.domain.invest.log.ProductInvestLog;
import me.zeroest.kyd_kakaopay.domain.invest.user.ProductInvestUserId;
import me.zeroest.kyd_kakaopay.domain.product.Product;
import me.zeroest.kyd_kakaopay.domain.product.status.ProductInvestStatus;
import me.zeroest.kyd_kakaopay.dto.rabbitmq.InvestMessage;
import me.zeroest.kyd_kakaopay.exception.BaseCustomException;
import me.zeroest.kyd_kakaopay.exception.ExceptionCode;
import me.zeroest.kyd_kakaopay.repository.invest.status.ProductInvestStatusRepository;
import me.zeroest.kyd_kakaopay.repository.invest.log.ProductInvestLogRepository;
import me.zeroest.kyd_kakaopay.repository.invest.user.ProductInvestUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvestSuccessServiceTest {

    @Mock
    private ProductInvestLogRepository productInvestLogRepository;
    @Mock
    private ProductInvestStatusRepository productInvestStatusRepository;
    @Mock
    private ProductInvestUserRepository productInvestUserRepository;

    @Mock
    private ProductRedisService productRedisService;

    @InjectMocks
    private InvestSuccessService investSuccessService;

    private long productId = 1L;
    private long investLogId = 1L;
    private InvestMessage investMessage = new InvestMessage(productId + RabbitConfig.MESSAGE_SPLIT + investLogId);


    @DisplayName("investSuccess - 성공")
    @Test
    void investSuccess() {

        when(productInvestLogRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(
                        ProductInvestLog.builder()
                                .build()
                ));

        when(productInvestStatusRepository.findByProductId(any(Long.class)))
                .thenReturn(Optional.of(
                        ProductInvestStatus.builder()
                                .id(investLogId)
                                .product(
                                        Product.builder()
                                                .id(productId)
                                                .totalAmount(1000L)
                                                .build()
                                )
                                .build()
                ));

        when(productRedisService.getInvestedAmount(any(Product.class)))
                .thenReturn(100L);
        when(productRedisService.getInvestingCnt(any(Product.class)))
                .thenReturn(10L);

        when(productInvestUserRepository.findById(any(ProductInvestUserId.class)))
                .thenReturn(Optional.empty());

        assertDoesNotThrow(() -> investSuccessService.investSuccess(investMessage));

    }



    @DisplayName("investSuccess - 전달받은 investLogId 가 데이터베이스에 존재하지 않을시 NOT_EXIST_INVEST_LOG 발생")
    @Test
    void notExistInvestLog() {

        when(productInvestLogRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());

        final BaseCustomException notExistInvestLog = assertThrows(
                BaseCustomException.class,
                () -> investSuccessService.investSuccess(investMessage)
        );

        assertEquals(ExceptionCode.NOT_EXIST_INVEST_LOG.getCode(), notExistInvestLog.getCode());
        assertEquals(ExceptionCode.NOT_EXIST_INVEST_LOG.getMessage() + investLogId, notExistInvestLog.getMessage());

    }

    @DisplayName("investSuccess - 전달받은 productId 의 investStatus 데이터가 존재하지 않을시 NOT_EXIST_PRODUCT 발생")
    @Test
    void notExistInvestStatus() {

        when(productInvestLogRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(
                        ProductInvestLog.builder()
                                .build()
                ));

        when(productInvestStatusRepository.findByProductId(any(Long.class)))
                .thenReturn(Optional.empty());

        final BaseCustomException notExistProduct = assertThrows(
                BaseCustomException.class,
                () -> investSuccessService.investSuccess(investMessage)
        );

        assertEquals(ExceptionCode.NOT_EXIST_PRODUCT.getCode(), notExistProduct.getCode());
        assertEquals(ExceptionCode.NOT_EXIST_PRODUCT.getMessage(), notExistProduct.getMessage());

    }

    @DisplayName("investSuccess - 전달받은 productId 의 product 데이터가 존재하지 않을시 NOT_EXIST_PRODUCT 발생")
    @Test
    void notExistProduct() {

        when(productInvestLogRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(
                        ProductInvestLog.builder()
                                .build()
                ));

        when(productInvestStatusRepository.findByProductId(any(Long.class)))
                .thenReturn(Optional.of(
                        ProductInvestStatus.builder().build()
                ));

        final BaseCustomException notExistProduct = assertThrows(
                BaseCustomException.class,
                () -> investSuccessService.investSuccess(investMessage)
        );

        assertEquals(ExceptionCode.NOT_EXIST_PRODUCT.getCode(), notExistProduct.getCode());
        assertEquals(ExceptionCode.NOT_EXIST_PRODUCT.getMessage(), notExistProduct.getMessage());

    }

}