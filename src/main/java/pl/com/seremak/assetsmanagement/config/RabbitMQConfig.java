package pl.com.seremak.assetsmanagement.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static pl.com.seremak.simplebills.commons.constants.MessageQueue.TRANSACTION_CREATION_REQUEST_QUEUE;

@Slf4j
@EnableRabbit
@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {

    private final CachingConnectionFactory cachingConnectionFactory;
    private final ObjectMapper objectMapper;


    @Bean
    public RabbitTemplate rabbitTemplate() {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    /**
     * Required for executing administration functions against an AMQP Broker
     */
    @Bean
    public AmqpAdmin rabbitAdmin() {
        return new RabbitAdmin(cachingConnectionFactory);
    }

    @Bean
    public Queue transactionCreationQueue() {
        return new Queue(TRANSACTION_CREATION_REQUEST_QUEUE, false);
    }
}
