package me.zeroest.kyd_kakaopay.config.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;

public class RabbitConfig {

    public static final String REQUEST_INVEST_EXCHANGE = "request.invest";
    public static final String INVEST_QUEUE_NAME = "queue.invest";

    public static final String MESSAGE_SPLIT = ":";
    public static String makeInvestSuccessMessage(long productId, long investLogId) {
        return productId + MESSAGE_SPLIT + investLogId;
    }

    @Bean
    Queue queue() {
        return new Queue(INVEST_QUEUE_NAME, true);
    }

}
