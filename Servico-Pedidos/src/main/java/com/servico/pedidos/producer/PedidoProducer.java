package com.servico.pedidos.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PedidoProducer {
	
	private static final Logger logger = LoggerFactory.getLogger(PedidoProducer.class);

	private final RabbitTemplate rabbitTemplate;
	
	@Value("${rabbitmq.exchange.name}")
	private String exchangeName;
	
	@Value("${rabbitmq.routing-key}")
	private String routingKey;

    @Value("${rabbitmq.notificacao.routing-key}")
    private String routingKeyNotificacao;

	public PedidoProducer(RabbitTemplate rabbitTemplate) {
		super();
		this.rabbitTemplate = rabbitTemplate;
	}
	
    public void enviarPedidoCriado(Object obj) {
        logger.info("Enviando mensagem para exchange: {}, routingKey: {}", exchangeName, routingKey);
        logger.info("Conteúdo da mensagem: {}", obj);
        
        try {
            rabbitTemplate.convertAndSend(exchangeName, routingKey, obj);
            logger.info("1- Mensagem enviada para fila de pedidos com sucesso!");
            
            rabbitTemplate.convertAndSend(exchangeName, routingKeyNotificacao, obj);
            logger.info("2- Mensagem enviada para fila de notificao com sucesso!");
        } catch (Exception e) {
            logger.error("Erro ao enviar mensagem: ", e);
            throw e;
        }
    }

}
