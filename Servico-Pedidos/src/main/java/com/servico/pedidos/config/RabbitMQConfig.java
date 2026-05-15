package com.servico.pedidos.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
	
	@Value("${rabbitmq.exchange.name}")
	private String exchangeName;
	
	@Value("${rabbitmq.queue.name}")
    private String queueName;

    @Value("${rabbitmq.routing-key}")
    private String routingKey;
	
    @Bean
    public Queue pedidoQueue() {
        return new Queue(queueName, true);
    }

    @Bean
    public DirectExchange pedidoExchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Binding pedidoBinding(Queue pedidoQueue, DirectExchange pedidoExchange) {
        return BindingBuilder
                .bind(pedidoQueue)
                .to(pedidoExchange)
                .with(routingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
