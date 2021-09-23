package me.zeroest.kyd_kakaopay.dto.product;

import lombok.Getter;
import lombok.ToString;
import me.zeroest.kyd_kakaopay.domain.product.InvestStatus;
import me.zeroest.kyd_kakaopay.domain.product.Product;

import java.time.LocalDateTime;

@Getter
@ToString
public class ProductDto {

    public ProductDto(Product product, long investedAmount, long investingCnt) {
        this.productId = product.getId();
        this.title = product.getTitle();
        this.totalInvestingAmount = product.getTotalAmount();
        this.startedAt = product.getStartedAt();
        this.finishedAt = product.getFinishedAt();

        this.currentInvestingAmount = investedAmount;
        this.investingCnt = investingCnt;

        this.investStatus = InvestStatus.ONGOING;
        if (totalInvestingAmount <= investedAmount) {
            investStatus = InvestStatus.COMPLETE;
        }
    }

    private Long productId;

    private String title;

    private Long totalInvestingAmount;

    private Long currentInvestingAmount;

    private Long investingCnt;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    private InvestStatus investStatus;

}
