package com.servico.notificacao.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.servico.notificacao.events.PedidoCriadoEvent;
import com.servico.notificacao.services.EmailService;

@Component
public class NotificacaoConsumer {
	
	private final Logger logger = LoggerFactory.getLogger(NotificacaoConsumer.class);
	private final EmailService emailService;
	
	public NotificacaoConsumer(EmailService emailService) {
		super();
		this.emailService = emailService;
	}
	
	@RabbitListener(queues = "queue.v1.ecommerce-pedido.fila.notificacao")
	public void enviarNotificacao(PedidoCriadoEvent evento) {
		logger.info("Tentando consumir a mensagem...");
		emailService.enviarEmail(evento);
		logger.info("Notificacao gerada: {}", evento.toString());
	}

}
