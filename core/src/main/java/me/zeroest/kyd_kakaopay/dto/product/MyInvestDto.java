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
    public MyInvestDto(Long productId, String userId, String title, Long totalInvestingAmount, Long myInvestingAmount, LocalDateTime investDate) {
        this.productId = productId;
        this.userId = userId;
        this.title = title;
        this.totalInvestingAmount = totalInvestingAmount;
        this.myInvestingAmount = myInvestingAmount;
        this.investDate = investDate;
    }

    private Long productId;

    private String userId;

    private String title;

    private Long totalInvestingAmount;

    private Long myInvestingAmount;

    private LocalDateTime investDate;

}
