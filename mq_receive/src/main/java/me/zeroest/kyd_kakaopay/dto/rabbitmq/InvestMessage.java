package me.zeroest.kyd_kakaopay.dto.rabbitmq;

import lombok.Getter;
import lombok.ToString;
import me.zeroest.kyd_kakaopay.config.rabbitmq.RabbitConfig;
import me.zeroest.kyd_kakaopay.exception.BaseCustomException;

import static me.zeroest.kyd_kakaopay.exception.ExceptionCode.RABBITMQ_INVALID_MESSAGE;

@Getter
@ToString
public class InvestMessage {

    private static final String DEFAULT_MESSAGE = RABBITMQ_INVALID_MESSAGE.getMessage();
    public static final String INVALID_MESSAGE_LENGHT = DEFAULT_MESSAGE + "LENGTH";
    public static final String INVALID_MESSAGE_NUMBER_FORMAT = DEFAULT_MESSAGE + "NUMBER_FORMAT";

    public InvestMessage(String messageBody) {
        final String[] messageSplit = messageBody.split(RabbitConfig.MESSAGE_SPLIT);
        if(messageSplit.length != 2){
            throw new BaseCustomException(INVALID_MESSAGE_LENGHT, RABBITMQ_INVALID_MESSAGE.getCode());
        }

        try {
            this.productId = Long.parseLong(messageSplit[0]);
            this.investLogId = Long.parseLong(messageSplit[1]);
        }catch (NumberFormatException exception){
            throw new BaseCustomException(INVALID_MESSAGE_NUMBER_FORMAT, RABBITMQ_INVALID_MESSAGE.getCode());
        }
    }

    private long productId;

    private long investLogId;

}
