package me.zeroest.kyd_kakaopay.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class BaseCustomException extends RuntimeException {

    public BaseCustomException(String message, String code, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
    }

    public BaseCustomException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.code = exceptionCode.getCode();
        this.status = HttpStatus.OK;
    }

    public BaseCustomException(String message, String code) {
        super(message);
        this.code = code;
        this.status = HttpStatus.OK;
    }

    public BaseCustomException(Object data, String message, String code) {
        super(message);
        this.code = code;
        this.status = HttpStatus.OK;
        this.data = data;
    }

    @Getter
    private String code;
    @Getter
    private HttpStatus status;
    @Getter
    private Object data;

}
