package me.zeroest.kyd_kakaopay.dto;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@ToString
public class InvestProductRequest {

    @NotNull
    @Min(value = 0L, message = "productId must be more than 0")
    private Long productId;

    @NotNull
    @Min(value = 1L, message = "investAmount must be more than 1")
    private Long investAmount;

}
