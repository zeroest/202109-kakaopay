package me.zeroest.kyd_kakaopay.dto.request;

public abstract class CommonRequest {
    public static final String X_USER_ID = "X-USER-ID";

    public static boolean isXUserId(String headerName) {
        return X_USER_ID.equals(headerName);
    }
}
