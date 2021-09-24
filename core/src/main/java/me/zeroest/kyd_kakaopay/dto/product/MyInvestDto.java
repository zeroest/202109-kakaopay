package me.zeroest.kyd_kakaopay.dto.product;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import me.zeroest.kyd_kakaopay.domain.invest.log.InvestResult;

import java.time.LocalDateTime;

@Getter
@ToString
public class MyInvestDto {

    @Builder
    @QueryProjection
    public MyInvestDto(Long logId, Long productId, String title, Long totalInvestingAmount, Long myInvestingAmount, LocalDateTime investDate, Long accrueUserInvest, InvestResult investResult, String userId) {
        this.logId = logId;
        this.productId = productId;
        this.title = title;
        this.totalInvestingAmount = totalInvestingAmount;
        this.myInvestingAmount = myInvestingAmount;
        this.investDate = investDate;
        this.accrueUserInvest = accrueUserInvest;
        this.investResult = investResult;
        this.userId = userId;
    }

    private Long logId;

    private Long productId;

    private String title;

    private Long totalInvestingAmount;

    private Long myInvestingAmount;

    private LocalDateTime investDate;

    private Long accrueUserInvest;

    private InvestResult investResult;

    private String userId;

}
