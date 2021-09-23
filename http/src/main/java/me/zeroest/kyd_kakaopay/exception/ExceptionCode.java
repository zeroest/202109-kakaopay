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
    NOT_EXIST_ITEM("NOT_EXIST_ITEM", "Not exist item : "),


    ;

    private String code;
    private String message;

}
