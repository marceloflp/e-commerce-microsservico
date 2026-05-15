package com.servico.pedidos.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.servico.pedidos.dtos.PedidoRequestDTO;
import com.servico.pedidos.dtos.PedidoResponseDTO;
import com.servico.pedidos.entities.Pedido;
import com.servico.pedidos.repositories.PedidoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PedidoService {
	
	private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);

	private final PedidoRepository pedidoRepository;
	

	public PedidoService(PedidoRepository pedidoRepository) {
		super();
		this.pedidoRepository = pedidoRepository;
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
		
		Pedido pedido = new Pedido(null, dto.nomeCliente(), dto.dataPedido(), dto.status(), dto.valorTotal());
	
		
		pedidoRepository.save(pedido);
		
		
		logger.info("Pedido criado: {}", pedido);
		
		
		return pedido;
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
		return new PedidoResponseDTO(pedido.getIdPedido(), pedido.getNomeCliente(), pedido.getDataPedido(), 
				pedido.getStatus(), pedido.getValorTotal());
	}
	
}
