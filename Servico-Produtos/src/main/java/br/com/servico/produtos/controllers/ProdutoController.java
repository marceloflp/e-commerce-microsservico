package br.com.servico.produtos.controllers;

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

import br.com.servico.produtos.dtos.ProdutoRequestDTO;
import br.com.servico.produtos.dtos.ProdutoResponseDTO;
import br.com.servico.produtos.entities.Produto;
import br.com.servico.produtos.services.ProdutoService;

@RestController
@RequestMapping
public class ProdutoController {

	private final ProdutoService produtoService;

	public ProdutoController(ProdutoService produtoService) {
		super();
		this.produtoService = produtoService;
	}
	
	@GetMapping("/buscarTodos")
	public ResponseEntity<List<ProdutoResponseDTO>> buscarTodos(){
		List<ProdutoResponseDTO> produtos = produtoService.buscarTodos();
		
		return ResponseEntity.ok().body(produtos);
	}
	
	@GetMapping("/buscarPorId/id/{id}")
	public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id){
		ProdutoResponseDTO produto = produtoService.buscarPorId(id);
		
		return ResponseEntity.ok().body(produto);
	}
	
	@PostMapping("/adicionar")
	public ResponseEntity<ProdutoResponseDTO> adicionar(@RequestBody ProdutoRequestDTO dto){
		Produto produto = produtoService.adicionarProduto(dto);
		ProdutoResponseDTO produtoDTO = produtoService.toDTO(produto);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(produto.getIdProduto())
				.toUri();
		
		return ResponseEntity.created(uri).body(produtoDTO);
	}
	
	@PutMapping("/atualizar/id/{id}")
	public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Long id, @RequestBody ProdutoRequestDTO dto){
		Produto produto = produtoService.atualizarProduto(id, dto);
		ProdutoResponseDTO produtoDTO = produtoService.toDTO(produto);
		
		return ResponseEntity.ok().body(produtoDTO);
		
	}
	
	@DeleteMapping("/deletar/id/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id){
		produtoService.deletarProduto(id);
		return ResponseEntity.noContent().build();
	}
}
