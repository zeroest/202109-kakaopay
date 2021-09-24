package me.zeroest.kyd_kakaopay.dto.product;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class MyInvestDto {

    private Long productId;

    private String title;

    private Long totalInvestingAmount;

    private Long myInvestingAmount;

    private LocalDateTime investDate;

}
