package com.servico.pedidos.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.servico.pedidos.dtos.ItemPedidoRequestDTO;
import com.servico.pedidos.dtos.ItemPedidoResponseDTO;
import com.servico.pedidos.entities.ItemPedido;
import com.servico.pedidos.services.ItemPedidoService;

@RestController
@RequestMapping("/api/itemPedidos")
public class ItemPedidoController {

	private final ItemPedidoService itemPedidoService;

	public ItemPedidoController(ItemPedidoService itemPedidoService) {
		super();
		this.itemPedidoService = itemPedidoService;
	}
	
	@GetMapping("/buscarTodos")
	public ResponseEntity<List<ItemPedidoResponseDTO>> buscarTodos(){
		List<ItemPedidoResponseDTO> itemPedidos = itemPedidoService.buscarTodos();
		
		return ResponseEntity.ok().body(itemPedidos);
	}
	
	@GetMapping("/buscarPorId/id/{id}")
	public ResponseEntity<ItemPedidoResponseDTO> buscarPorId(@PathVariable Long id){
		ItemPedidoResponseDTO itemPedido = itemPedidoService.buscarPorId(id);
		
		return ResponseEntity.ok().body(itemPedido);
	}
	
	@PostMapping("/adicionar")
	public ResponseEntity<ItemPedidoResponseDTO> adicionar(@RequestBody ItemPedidoRequestDTO dto){
		ItemPedido itemPedido = itemPedidoService.adicionarItemPedido(dto);
		ItemPedidoResponseDTO itemPedidoDTO = itemPedidoService.toDTO(itemPedido);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(itemPedido.getIdItem())
				.toUri();
		
		return ResponseEntity.created(uri).body(itemPedidoDTO);
	}
	
	@PutMapping("/atualizar/id/{id}")
	public ResponseEntity<ItemPedidoResponseDTO> atualizar(@PathVariable Long id, @RequestBody ItemPedidoRequestDTO dto){
		ItemPedido itemPedido = itemPedidoService.atualizarItemPedido(id, dto);
		ItemPedidoResponseDTO itemPedidoDTO = itemPedidoService.toDTO(itemPedido);
		
		return ResponseEntity.ok().body(itemPedidoDTO);
		
	}
	
	@DeleteMapping("/deletar/id/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id){
		itemPedidoService.deletarItemPedido(id);
		return ResponseEntity.noContent().build();
	}
}
