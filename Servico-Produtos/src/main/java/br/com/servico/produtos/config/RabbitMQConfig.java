package br.com.servico.produtos.config;

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
    public Queue produtoQueue() {
        return new Queue(queueName, true);
    }

    @Bean
    public DirectExchange produtoExchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Binding produtoBinding(Queue produtoQueue, DirectExchange produtoExchange) {
        return BindingBuilder
                .bind(produtoQueue)
                .to(produtoExchange)
                .with(routingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
