package me.zeroest.kyd_kakaopay.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zeroest.kyd_kakaopay.domain.invest.log.ProductInvestLog;
import me.zeroest.kyd_kakaopay.domain.invest.user.ProductInvestUser;
import me.zeroest.kyd_kakaopay.domain.invest.user.ProductInvestUserId;
import me.zeroest.kyd_kakaopay.domain.product.Product;
import me.zeroest.kyd_kakaopay.domain.product.status.ProductInvestStatus;
import me.zeroest.kyd_kakaopay.dto.rabbitmq.InvestMessage;
import me.zeroest.kyd_kakaopay.exception.BaseCustomException;
import me.zeroest.kyd_kakaopay.repository.invest.status.ProductInvestStatusRepository;
import me.zeroest.kyd_kakaopay.repository.invest.log.ProductInvestLogRepository;
import me.zeroest.kyd_kakaopay.repository.invest.user.ProductInvestUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

import static me.zeroest.kyd_kakaopay.exception.ExceptionCode.NOT_EXIST_INVEST_LOG;
import static me.zeroest.kyd_kakaopay.exception.ExceptionCode.NOT_EXIST_PRODUCT;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvestService {

    private final ProductRedisService productRedisService;

    private final ProductInvestStatusRepository productInvestStatusRepository;
    private final ProductInvestLogRepository productInvestLogRepository;
    private final ProductInvestUserRepository productInvestUserRepository;

    @Transactional
    public void investSuccess(InvestMessage investMessage) {

        // Get log data
        final ProductInvestLog productInvestLog = productInvestLogRepository.findById(investMessage.getInvestLogId())
                .orElseThrow(() -> new BaseCustomException(
                        NOT_EXIST_INVEST_LOG.getMessage() + investMessage.getInvestLogId()
                        , NOT_EXIST_INVEST_LOG.getCode()
                        )
                );

        try{

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
            productInvestStatus.applyInvestResult(
                    product.getTotalAmount(),
                    productRedisService.getInvestedAmount(product),
                    productRedisService.getInvestingCnt(product)
            );

            // Update invest user
            final ProductInvestUserId productInvestUserId = new ProductInvestUserId(productInvestLog.getUserId(), product);
            final Optional<ProductInvestUser> optionalInvestUser = productInvestUserRepository.findById(productInvestUserId);

            if (optionalInvestUser.isPresent()) {
                final ProductInvestUser productInvestUser = optionalInvestUser.get();
                productInvestUser.updateTotalInvestAmount(productInvestLog.getAccrueUserInvest());
            } else {
                productInvestUserRepository.save(
                        ProductInvestUser.builder()
                                .id(productInvestUserId)
                                .investAmount(productInvestLog.getAccrueUserInvest())
                                .build()
                );
            }

        }catch (Exception e){
            productInvestLog.updateLogFail(e.getMessage());
        }

    }

}
