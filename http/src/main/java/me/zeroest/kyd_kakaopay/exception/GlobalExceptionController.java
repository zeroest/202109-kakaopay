package me.zeroest.kyd_kakaopay.exception;

import lombok.extern.slf4j.Slf4j;
import me.zeroest.kyd_kakaopay.dto.request.CommonRequest;
import me.zeroest.kyd_kakaopay.util.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import static me.zeroest.kyd_kakaopay.exception.ExceptionCode.MISSING_X_USER_ID;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionController {

    @ExceptionHandler({Exception.class})
    public ResponseEntity exception(Exception exception) {
        log.error("GlobalException:exception" + exception.getClass().toString());

        return ApiUtil.fail(exception, "500", exception.getMessage());

    }

    @ExceptionHandler({NoHandlerFoundException.class})
    public ResponseEntity handlerError404(Exception exception) {
        log.error("GlobalException:handlerError404" + exception.getClass().toString());

        return ApiUtil.fail(exception, "404", exception.getMessage(), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler({BaseCustomException.class})
    public ResponseEntity baseCustomException(BaseCustomException exception) {
        log.error("GlobalException:baseCustomException" + exception.getClass().toString());

        return ApiUtil.fail(exception, exception.getCode(), exception.getMessage(), exception.getStatus());

    }

    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    public ResponseEntity bindingResult(Exception exception) {
        log.error("GlobalException:bindingResult" + exception.getClass().toString());

        BindingResult bindingResult = null;
        if (exception instanceof BindException) {
            bindingResult = ((BindException) exception).getBindingResult();
        } else {
            bindingResult = ((MethodArgumentNotValidException) exception).getBindingResult();
        }

        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("](???)??? ");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" ????????? ???: [");
            builder.append(fieldError.getRejectedValue());
            builder.append("]  ");
        }

        return ApiUtil.fail(exception, "00", builder.toString(), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler({MissingRequestHeaderException.class})
    public ResponseEntity missingHeader(MissingRequestHeaderException exception) {
        log.error("GlobalException:missingHeader" + exception.getClass().toString());

        if(CommonRequest.isXUserId(exception.getHeaderName())){
            return ApiUtil.fail(exception, MISSING_X_USER_ID.getCode(), MISSING_X_USER_ID.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return ApiUtil.fail(exception, exception.getClass().toString(), exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
