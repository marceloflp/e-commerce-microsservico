package com.servico.pedidos.services;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.servico.pedidos.dtos.ItemPedidoResponseDTO;
import com.servico.pedidos.dtos.PedidoRequestDTO;
import com.servico.pedidos.dtos.PedidoResponseDTO;
import com.servico.pedidos.dtos.ProdutoResponseDTO;
import com.servico.pedidos.entities.ItemPedido;
import com.servico.pedidos.entities.Pedido;
import com.servico.pedidos.events.ItemPedidoEventDTO;
import com.servico.pedidos.events.PedidoCriadoEvent;
import com.servico.pedidos.producer.PedidoProducer;
import com.servico.pedidos.repositories.ItemPedidoRepository;
import com.servico.pedidos.repositories.PedidoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PedidoService {
	
	private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);

	private final PedidoRepository pedidoRepository;
	private final ProdutoClient produtoClient;
	private final ItemPedidoRepository itemPedido;
	private final PedidoProducer producer;

	public PedidoService(PedidoRepository pedidoRepository, ProdutoClient client, ItemPedidoRepository itemPedidoRepository, PedidoProducer producer) {
		super();
		this.pedidoRepository = pedidoRepository;
		this.produtoClient = client;
		this.itemPedido = itemPedidoRepository;
		this.producer = producer;
	}
	
	public List<PedidoResponseDTO> buscarTodos(){
		logger.info("Buscando todos os pedidos cadastrados");
		List<Pedido> pedidos = pedidoRepository.findAll();
		
		return pedidos.stream().map(this::toDTO).collect(Collectors.toList());
	}
	
	
	public PedidoResponseDTO buscarPorId(Long id) {
		
		logger.info("Buscando pedido de id {}", id);
		
		Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));
		
		return toDTO(pedido);
	}
	
	public Pedido adicionarPedido(PedidoRequestDTO dto) {

	    logger.info("Adicionando novo pedido...");

	    Pedido pedido = new Pedido();
	    pedido.setNomeCliente(dto.nomeCliente());
	    pedido.setStatus(dto.status());
	    pedido.setDataPedido(dto.dataPedido());
	    
	  //Map de ID e quantidade de produtos para enviar para o serviço de produto para atualizar estoque
	    Map<Long, Integer> produtosEstoques = new HashMap<>();

	    List<ItemPedido> itens = dto.itens()
	            .stream()
	            .map(itemDTO -> {

	                ProdutoResponseDTO produto =
	                        produtoClient.buscarProduto(itemDTO.idProduto());

	                ItemPedido item = new ItemPedido();

	                item.setNomeProduto(produto.nome());
	                item.setPrecoProduto(produto.preco());
	                item.setQuantidade(itemDTO.quantidade());
	                
	                item.setIdProduto(produto.id());	

	                item.setPedido(pedido);
	                
	                produtosEstoques.put(itemDTO.idProduto(), itemDTO.quantidade());

	                return item;

	            }).toList();

	    BigDecimal totalPago = itens.stream()
	            .map(item -> item.getPrecoProduto()
	                    .multiply(BigDecimal.valueOf(item.getQuantidade())))
	            .reduce(BigDecimal.ZERO, BigDecimal::add);

	    pedido.setItens(itens);
	    pedido.setValorTotal(totalPago);

	    Pedido pedidoSalvo = pedidoRepository.save(pedido);
	    
	    //Montar para enviar para as filas
	    PedidoCriadoEvent evento = new PedidoCriadoEvent(

	            pedidoSalvo.getIdPedido(),
	            pedidoSalvo.getNomeCliente(),
	            pedidoSalvo.getValorTotal(),

	            dto.itens().stream()
	                    .map(item -> new ItemPedidoEventDTO(
	                            item.idProduto(),
	                            item.quantidade()
	                    ))
	                    .toList(),
	            pedidoSalvo.getEmailNotificacao()
	    );
	    
	    //Enviar aqui abaixo
	    producer.enviarPedidoCriado(evento);
	    

	    logger.info("Pedido criado: {}", pedidoSalvo);

	    return pedidoSalvo;
	}
	
	public Pedido atualizarPedido(Long id, PedidoRequestDTO dto) {
		
		logger.info("Atualizando pedido. ID: {}", id);
		
		try {
			Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));
			updatePedido(dto, pedido);
			pedidoRepository.save(pedido);
			logger.info("Pedido de ID {} atualizado com sucesso!", id);
			
			return pedido;
		} catch (EntityNotFoundException e) {
			logger.error("Erro ao atualizar: Pedido não encontrado");
            throw new EntityNotFoundException("Pedido não encotrado!");
        }
		
	}
	
	private void updatePedido(PedidoRequestDTO dto, Pedido pedido) {
		pedido.setDataPedido(dto.dataPedido());
		pedido.setNomeCliente(dto.nomeCliente());
		pedido.setStatus(dto.status());
		pedido.setValorTotal(dto.valorTotal());
	}
	
	public void deletarPedido(Long id) {
		logger.info("Deletando pedido. ID: {}", id);
		try {
			if(!pedidoRepository.existsById(id)) {
				throw new EntityNotFoundException("Pedido não encontrado");
			}
			pedidoRepository.deleteById(id);
			logger.info("Pedido deletado com sucesso!");
		} catch (Exception e) {
			logger.error("Erro ao deletar pedido: {}", e.toString());
			throw new RuntimeException("Exceção genérica para teste");
		}
	}
	
	public PedidoResponseDTO toDTO(Pedido pedido) {
		
		List<ItemPedidoResponseDTO> itens = pedido.getItens()
				.stream()
				.map(itensDTO -> {
					ItemPedidoResponseDTO itemDTO = new ItemPedidoResponseDTO(itensDTO.getIdItem(), itensDTO.getIdProduto(),
							itensDTO.getNomeProduto(), itensDTO.getPrecoProduto(), itensDTO.getQuantidade());
					return itemDTO;
				}).toList();
		
		return new PedidoResponseDTO(pedido.getIdPedido(), pedido.getNomeCliente(), pedido.getDataPedido(), 
				pedido.getStatus(), pedido.getValorTotal(), itens, pedido.getEmailNotificacao());
	}
	
}
