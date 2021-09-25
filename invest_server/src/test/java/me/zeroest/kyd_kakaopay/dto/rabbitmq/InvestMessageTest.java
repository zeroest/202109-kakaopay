package me.zeroest.kyd_kakaopay.dto.rabbitmq;

import me.zeroest.kyd_kakaopay.config.rabbitmq.RabbitConfig;
import me.zeroest.kyd_kakaopay.exception.BaseCustomException;
import me.zeroest.kyd_kakaopay.exception.ExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvestMessageTest {

    @DisplayName("메세지의 productId 또는 investLogId 가 존재하지 않을시 INVALID_MESSAGE 반환")
    @Test
    void invalidMessageLength() {

        long productId = 1L;
        long investLogId = 1L;

        String message1 = productId + RabbitConfig.MESSAGE_SPLIT;
        final BaseCustomException invalidLength1 = assertThrows(
                BaseCustomException.class,
                () -> new InvestMessage(message1)
        );

        assertEquals(InvestMessage.INVALID_MESSAGE_LENGHT, invalidLength1.getMessage());
        assertEquals(ExceptionCode.RABBITMQ_INVALID_MESSAGE.getCode(), invalidLength1.getCode());

    }

    @DisplayName("메세지의 productId 또는 investLogId 가 숫자로 입력되지 않을시 INVALID_MESSAGE_NUMBER_FORMAT 반환")
    @Test
    void invalidNumberFormat() {

        long productId = 1L;
        long investLogId = 1L;

        String message1 = productId + RabbitConfig.MESSAGE_SPLIT + "a";
        final BaseCustomException invalidLength1 = assertThrows(
                BaseCustomException.class,
                () -> new InvestMessage(message1)
        );

        assertEquals(InvestMessage.INVALID_MESSAGE_NUMBER_FORMAT, invalidLength1.getMessage());
        assertEquals(ExceptionCode.RABBITMQ_INVALID_MESSAGE.getCode(), invalidLength1.getCode());


        String message2 = "a" + RabbitConfig.MESSAGE_SPLIT + investLogId;
        final BaseCustomException invalidLength2 = assertThrows(
                BaseCustomException.class,
                () -> new InvestMessage(message1)
        );

        assertEquals(InvestMessage.INVALID_MESSAGE_NUMBER_FORMAT, invalidLength2.getMessage());
        assertEquals(ExceptionCode.RABBITMQ_INVALID_MESSAGE.getCode(), invalidLength2.getCode());

    }

}