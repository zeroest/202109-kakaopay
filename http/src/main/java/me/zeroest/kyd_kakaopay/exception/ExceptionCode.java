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


    ;

    private String code;
    private String message;

}
