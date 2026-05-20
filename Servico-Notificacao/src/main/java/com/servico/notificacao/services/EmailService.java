package com.servico.notificacao.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.servico.notificacao.events.PedidoCriadoEvent;

@Service
public class EmailService {
	
	private final JavaMailSender mailSender;

	public EmailService(JavaMailSender mailSender) {
		super();
		this.mailSender = mailSender;
	}
	
	public void enviarEmail(PedidoCriadoEvent evento) {
		SimpleMailMessage mensagem = new SimpleMailMessage();
		
		mensagem.setFrom("ecommerce-pedidos@email.com");
		mensagem.setTo(evento.emailNotificacao());
		mensagem.setSubject("Pedido de Compra");
		mensagem.setText(gerarMensagem(evento));
		
		mailSender.send(mensagem);
	}
	
	public String gerarMensagem(PedidoCriadoEvent evento) {
		String mensagem = "Olá senhor(a) "  + evento.nomeCliente().toString() + ", seu pedido de valor " + evento.valorTotal().toString() +
				" foi efetuado com sucesso! Aproveite bem!";
		
		return mensagem;
	}

}
