package me.zeroest.kyd_kakaopay.listener.rabbitmq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zeroest.kyd_kakaopay.config.rabbitmq.RabbitConfig;
import me.zeroest.kyd_kakaopay.dto.rabbitmq.InvestMessage;
import me.zeroest.kyd_kakaopay.service.InvestService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvestMessageListener {

    private final InvestService investService;

    @RabbitListener(queues = RabbitConfig.INVEST_QUEUE_NAME)
    public void investMessage(final Message message) {
        log.info("message : {}", message);
        final String body = new String(message.getBody(), StandardCharsets.UTF_8);

        try {
            investService.investSuccess(new InvestMessage(body));
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }

    }

}
