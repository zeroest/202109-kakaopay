package me.zeroest.kyd_kakaopay.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class CommonResponse<T> {
    public static final String OK = "OK";
    public static final String ERROR = "ERROR";

    public CommonResponse(boolean result, String code, String message, T data) {
        this.result = result;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    protected boolean result;
    protected String code;
    protected String message;
    protected T data;
}
