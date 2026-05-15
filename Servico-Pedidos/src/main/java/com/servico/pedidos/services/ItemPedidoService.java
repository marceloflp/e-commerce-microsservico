package com.servico.pedidos.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.servico.pedidos.dtos.ItemPedidoRequestDTO;
import com.servico.pedidos.dtos.ItemPedidoResponseDTO;
import com.servico.pedidos.entities.ItemPedido;
import com.servico.pedidos.repositories.ItemPedidoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ItemPedidoService {
	
	private static final Logger logger = LoggerFactory.getLogger(ItemPedidoService.class);

	private final ItemPedidoRepository itemPedidoRepository;
	

	public ItemPedidoService(ItemPedidoRepository itemPedidoRepository) {
		super();
		this.itemPedidoRepository = itemPedidoRepository;
	}
	
	public List<ItemPedidoResponseDTO> buscarTodos(){
		logger.info("Buscando todos os itens cadastrados");
		List<ItemPedido> itemPedidos = itemPedidoRepository.findAll();
		
		return itemPedidos.stream().map(this::toDTO).collect(Collectors.toList());
	}
	
	
	public ItemPedidoResponseDTO buscarPorId(Long id) {
		
		logger.info("Buscando item de id {}", id);
		
		ItemPedido itemPedido = itemPedidoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("ItemPedido não encontrado"));
		
		return toDTO(itemPedido);
	}
	
	public ItemPedido adicionarItemPedido(ItemPedidoRequestDTO dto) {
		
		logger.info("Adicionando novo item...");
		
		ItemPedido itemPedido = new ItemPedido(null, dto.idProduto(), dto.nomeProduto(), dto.precoProduto(), dto.quantidade());
	
		
		itemPedidoRepository.save(itemPedido);
		
		
		logger.info("ItemPedido criado: {}", itemPedido);
		
		
		return itemPedido;
	}
	
	public ItemPedido atualizarItemPedido(Long id, ItemPedidoRequestDTO dto) {
		
		logger.info("Atualizando item. ID: {}", id);
		
		try {
			ItemPedido itemPedido = itemPedidoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("ItemPedido não encontrado"));
			updateItemPedido(dto, itemPedido);
			itemPedidoRepository.save(itemPedido);
			logger.info("Item de ID {} atualizado com sucesso!", id);
			
			return itemPedido;
		} catch (EntityNotFoundException e) {
			logger.error("Erro ao atualizar: Item não encontrado");
            throw new EntityNotFoundException("Item não encotrado!");
        }
		
	}
	
	private void updateItemPedido(ItemPedidoRequestDTO dto, ItemPedido itemPedido) {
		itemPedido.setIdProduto(dto.idProduto());
		itemPedido.setNomeProduto(dto.nomeProduto());
		itemPedido.setPrecoProduto(dto.precoProduto());
		itemPedido.setQuantidade(dto.quantidade());
	}
	
	public void deletarItemPedido(Long id) {
		logger.info("Deletando item. ID: {}", id);
		try {
			if(!itemPedidoRepository.existsById(id)) {
				throw new EntityNotFoundException("Item não encontrado");
			}
			itemPedidoRepository.deleteById(id);
			logger.info("Item deletado com sucesso!");
		} catch (Exception e) {
			logger.error("Erro ao deletar item: {}", e.toString());
			throw new RuntimeException("Exceção genérica para teste");
		}
	}
	
	public ItemPedidoResponseDTO toDTO(ItemPedido itemPedido) {
		return new ItemPedidoResponseDTO(itemPedido.getIdItem(), itemPedido.getIdProduto(), itemPedido.getNomeProduto(),
				itemPedido.getPrecoProduto(), itemPedido.getQuantidade());
	}
	
}
