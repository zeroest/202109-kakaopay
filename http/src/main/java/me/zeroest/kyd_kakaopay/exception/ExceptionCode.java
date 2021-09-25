package me.zeroest.kyd_kakaopay.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

    /**
     * Common
     * */
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "Internal server error : "),
    MISSING_X_USER_ID("MISSING_X_USER_ID", "Missing X-USER-ID header value : "),

    /**
     * InvestController
     * */
    NOT_EXIST_PRODUCT("NOT_EXIST_PRODUCT", "Not exist product : "),
    INVEST_AMOUNT_OVERFLOW("INVEST_AMOUNT_OVERFLOW", "Invest amount more than total investing amount : "),
    SOLD_OUT("SOLD_OUT", "Product is sold out : "),

    ;

    private String code;
    private String message;

}
