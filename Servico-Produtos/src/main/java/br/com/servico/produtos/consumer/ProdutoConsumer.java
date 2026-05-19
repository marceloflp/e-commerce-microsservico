package br.com.servico.produtos.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.servico.produtos.entities.Produto;
import br.com.servico.produtos.events.ItemPedidoEventDTO;
import br.com.servico.produtos.events.PedidoCriadoEvent;
import br.com.servico.produtos.repositories.ProdutoRepository;

@Component
public class ProdutoConsumer {
	
	private final Logger logger = LoggerFactory.getLogger(ProdutoConsumer.class);
	
	@Value("${rabbitmq.queue.name}")
	private String queueName;
	
	private final ProdutoRepository produtoRepository;
	
	public ProdutoConsumer(ProdutoRepository produtoRepository) {
		super();
		this.produtoRepository = produtoRepository;
	}


	@RabbitListener(queues = "queue.v1.ecommerce-pedido.fila.criado")
	public void consumirPedido(PedidoCriadoEvent evento) {
		
		 try {

		        logger.info("Consumindo pedido...");

		        for(ItemPedidoEventDTO item : evento.itens()) {

		            Produto produto = produtoRepository
		                    .findById(item.idProduto())
		                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

		            produto.setEstoque(produto.getEstoque() - item.quantidade());

		            produtoRepository.save(produto);
		        }

		        logger.info("Pedido processado com sucesso");

		    } catch (Exception e) {

		        logger.error("Erro ao consumir mensagem", e);

		        throw e;
		    }
		
		
		
	}

}
