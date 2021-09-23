package me.zeroest.kyd_kakaopay.util;

import lombok.extern.slf4j.Slf4j;
import me.zeroest.kyd_kakaopay.dto.response.HttpResponse;
import me.zeroest.kyd_kakaopay.exception.BaseCustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

@Slf4j
public class ApiUtil {

    public static <T> ResponseEntity<T> success(T data, String code, String message, HttpStatus status, MultiValueMap headers){

        if (status == null) {

            status = HttpStatus.OK;

        }

        HttpResponse<T> httpResponse = new HttpResponse(true, code, message, data, status.value());

        return new ResponseEntity<>(httpResponse, headers, status);

    }

    public static <T> ResponseEntity<T> success(T data){
        return success(data, "01", null, null, null);
    }
    public static <T> ResponseEntity<T> success(T data, String code){
        return success(data, code, null, null, null);
    }
    public static <T> ResponseEntity<T> success(T data, String code, String message){
        return success(data, code, message, null, null);
    }
    public static <T> ResponseEntity<T> success(T data, String code, HttpStatus status){
        return success(data, code, null, status, null);
    }
    public static <T> ResponseEntity<T> success(T data, String code, String message, HttpStatus status){
        return success(data, code, message, status, null);
    }
    public static <T> ResponseEntity<T> success(MultiValueMap headers){
        return success(null, null, null, null, headers);
    }


    public static <T> ResponseEntity fail(T data, String code, String message, HttpStatus status, MultiValueMap headers) {

        if (status == null) {

            status = HttpStatus.INTERNAL_SERVER_ERROR;

        }

        Object resultData = null;

        if (data instanceof Exception) {

            Exception exception = (Exception) data;

            if (message == null || message.equals("")) {

                message = exception.getMessage();

            }

            log.error(message, exception);

            if (exception instanceof BaseCustomException) {
                resultData = ((BaseCustomException) exception).getData();
            }

        }

        HttpResponse httpResponse = new HttpResponse(false, code, message, resultData, status.value());

        return new ResponseEntity(httpResponse, headers, status);

    }

    public static <T> ResponseEntity fail(T data) {
        return fail(data, null, null, null, null);
    }
    public static <T> ResponseEntity fail(T data, String code) {
        return fail(data, code, null, null, null);
    }
    public static <T> ResponseEntity fail(T data, String code, String message) {
        return fail(data, code, message, null, null);
    }
    public static <T> ResponseEntity fail(T data, String code, HttpStatus status) {
        return fail(data, code, null, status, null);
    }
    public static <T> ResponseEntity fail(T data, String code, String message, HttpStatus status) {
        return fail(data, code, message, status, null);
    }

}
