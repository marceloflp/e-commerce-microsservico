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

import br.com.servico.produtos.dtos.CategoriaRequestDTO;
import br.com.servico.produtos.dtos.CategoriaResponseDTO;
import br.com.servico.produtos.entities.Categoria;
import br.com.servico.produtos.services.CategoriaService;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

	private final CategoriaService categoriaService;

	public CategoriaController(CategoriaService categoriaService) {
		super();
		this.categoriaService = categoriaService;
	}
	
	@GetMapping("/buscarTodos")
	public ResponseEntity<List<CategoriaResponseDTO>> buscarTodos(){
		List<CategoriaResponseDTO> categorias = categoriaService.buscarTodos();
		
		return ResponseEntity.ok().body(categorias);
	}
	
	@GetMapping("/buscarPorId/id/{id}")
	public ResponseEntity<CategoriaResponseDTO> buscarPorId(@PathVariable Long id){
		CategoriaResponseDTO categoria = categoriaService.buscarPorId(id);
		
		return ResponseEntity.ok().body(categoria);
	}
	
	@PostMapping("/adicionar")
	public ResponseEntity<CategoriaResponseDTO> adicionar(@RequestBody CategoriaRequestDTO dto){
		Categoria categoria = categoriaService.adicionarCategoria(dto);
		CategoriaResponseDTO categoriaDTO = categoriaService.toDTO(categoria);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(categoria.getIdCategoria())
				.toUri();
		
		return ResponseEntity.created(uri).body(categoriaDTO);
	}
	
	@PutMapping("/atualizar/id/{id}")
	public ResponseEntity<CategoriaResponseDTO> atualizar(@PathVariable Long id, @RequestBody CategoriaRequestDTO dto){
		Categoria categoria = categoriaService.atualizarCategoria(id, dto);
		CategoriaResponseDTO categoriaDTO = categoriaService.toDTO(categoria);
		
		return ResponseEntity.ok().body(categoriaDTO);
		
	}
	
	@DeleteMapping("/deletar/id/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id){
		categoriaService.deletarCategoria(id);
		return ResponseEntity.noContent().build();
	}
}
