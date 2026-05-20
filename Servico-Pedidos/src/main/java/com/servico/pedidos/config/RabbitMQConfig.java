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
    
    @Value("${rabbitmq.queue.notificacao}")
    private String queueNameNotificacao;

    @Value("${rabbitmq.notificacao.routing-key}")
    private String routingKeyNotificacao;
	
    @Bean
    public Queue pedidoQueue() {
        return new Queue(queueName, true);
    }

    @Bean
    public DirectExchange pedidoExchange() {
        return new DirectExchange(exchangeName);
    }
    
    @Bean
    public Queue notificacaoQueue() {
        return new Queue(queueNameNotificacao, true);
    }

    @Bean
    public Binding pedidoBinding(Queue pedidoQueue, DirectExchange pedidoExchange) {
        return BindingBuilder
                .bind(pedidoQueue)
                .to(pedidoExchange)
                .with(routingKey);
    }
    
    @Bean
    public Binding notificacaoBinding(Queue notificacaoQueue, DirectExchange pedidoExchange) {
        return BindingBuilder
                .bind(notificacaoQueue)
                .to(pedidoExchange)
                .with(routingKeyNotificacao);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
