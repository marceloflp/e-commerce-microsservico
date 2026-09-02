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

import com.servico.pedidos.dtos.PedidoRequestDTO;
import com.servico.pedidos.dtos.PedidoResponseDTO;
import com.servico.pedidos.entities.Pedido;
import com.servico.pedidos.services.PedidoService;

@RestController
@RequestMapping
public class PedidoController {

	private final PedidoService pedidoService;

	public PedidoController(PedidoService pedidoService) {
		super();
		this.pedidoService = pedidoService;
	}
	
	@GetMapping("/buscarTodos")
	public ResponseEntity<List<PedidoResponseDTO>> buscarTodos(){
		List<PedidoResponseDTO> pedidos = pedidoService.buscarTodos();
		
		return ResponseEntity.ok().body(pedidos);
	}
	
	@GetMapping("/buscarPorId/id/{id}")
	public ResponseEntity<PedidoResponseDTO> buscarPorId(@PathVariable Long id){
		PedidoResponseDTO pedido = pedidoService.buscarPorId(id);
		
		return ResponseEntity.ok().body(pedido);
	}
	
	@PostMapping("/adicionar")
	public ResponseEntity<PedidoResponseDTO> adicionar(@RequestBody PedidoRequestDTO dto){
		Pedido pedido = pedidoService.adicionarPedido(dto);
		PedidoResponseDTO pedidoDTO = pedidoService.toDTO(pedido);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(pedido.getIdPedido())
				.toUri();
		
		return ResponseEntity.created(uri).body(pedidoDTO);
	}
	
	@PutMapping("/atualizar/id/{id}")
	public ResponseEntity<PedidoResponseDTO> atualizar(@PathVariable Long id, @RequestBody PedidoRequestDTO dto){
		Pedido pedido = pedidoService.atualizarPedido(id, dto);
		PedidoResponseDTO pedidoDTO = pedidoService.toDTO(pedido);
		
		return ResponseEntity.ok().body(pedidoDTO);
		
	}
	
	@DeleteMapping("/deletar/id/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id){
		pedidoService.deletarPedido(id);
		return ResponseEntity.noContent().build();
	}
}
