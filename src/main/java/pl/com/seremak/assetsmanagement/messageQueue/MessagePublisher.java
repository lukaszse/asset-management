package pl.com.seremak.assetsmanagement.messageQueue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import pl.com.seremak.simplebills.commons.dto.http.TransactionDto;

import static pl.com.seremak.assetsmanagement.config.RabbitMQConfig.TRANSACTION_CREATION_REQUEST_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessagePublisher {

    private final RabbitTemplate rabbitTemplate;

    
    public void sentTransactionCreationRequest(final TransactionDto transactionDto) {
        rabbitTemplate.convertAndSend(transactionDto);
        log.info("Message sent: queue={}, message={}", TRANSACTION_CREATION_REQUEST_QUEUE, transactionDto);
    }
}
