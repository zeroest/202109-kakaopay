package me.zeroest.kyd_kakaopay.dto.response;

import lombok.*;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class HttpResponse<T> extends CommonResponse<T> {
    @Builder
    public HttpResponse(boolean result, String code, String message, T data, int status) {
        super(result, code, message, data);

        this.status = status;
    }

    private int status;
}
