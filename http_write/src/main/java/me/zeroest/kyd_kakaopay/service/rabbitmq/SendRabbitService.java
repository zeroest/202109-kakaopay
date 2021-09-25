package me.zeroest.kyd_kakaopay.service.rabbitmq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendRabbitService {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(String exchange, String message) {
        rabbitTemplate.convertAndSend(exchange, "", message);
        log.info("Send rabbitmq message = exchange: {}, message: {}", exchange, message);
    }

}
