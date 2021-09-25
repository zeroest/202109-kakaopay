package me.zeroest.kyd_kakaopay.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zeroest.kyd_kakaopay.domain.invest.log.ProductInvestLog;
import me.zeroest.kyd_kakaopay.domain.product.Product;
import me.zeroest.kyd_kakaopay.domain.product.status.ProductInvestStatus;
import me.zeroest.kyd_kakaopay.dto.rabbitmq.InvestMessage;
import me.zeroest.kyd_kakaopay.exception.BaseCustomException;
import me.zeroest.kyd_kakaopay.repository.ProductInvestStatusRepository;
import me.zeroest.kyd_kakaopay.repository.log.ProductInvestLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static me.zeroest.kyd_kakaopay.exception.ExceptionCode.NOT_EXIST_INVEST_LOG;
import static me.zeroest.kyd_kakaopay.exception.ExceptionCode.NOT_EXIST_PRODUCT;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvestService {

    private final ProductInvestStatusRepository productInvestStatusRepository;
    private final ProductInvestLogRepository productInvestLogRepository;

    @Transactional
    public void investSuccess(InvestMessage investMessage) {

        // Get log data
        final ProductInvestLog productInvestLog = productInvestLogRepository.findById(investMessage.getInvestLogId())
                .orElseThrow(() -> new BaseCustomException(NOT_EXIST_INVEST_LOG));

        // Update invest log
        productInvestLog.updateLogSuccess();


        // Get product status
        final ProductInvestStatus productInvestStatus = productInvestStatusRepository.findByProductId(investMessage.getProductId())
                .orElseThrow(() -> new BaseCustomException(NOT_EXIST_PRODUCT));
        final Product product = productInvestLog.getProduct();
        if (Objects.isNull(product)) {
            throw new BaseCustomException(NOT_EXIST_PRODUCT);
        }

        // Update invest status
        productInvestStatus.applyInvestResult(product.getTotalAmount(), productInvestLog.getAccrueProductInvest());

    }

}
