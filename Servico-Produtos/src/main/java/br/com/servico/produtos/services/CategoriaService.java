package br.com.servico.produtos.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.servico.produtos.dtos.CategoriaRequestDTO;
import br.com.servico.produtos.dtos.CategoriaResponseDTO;
import br.com.servico.produtos.entities.Categoria;
import br.com.servico.produtos.repositories.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoriaService {

	private final CategoriaRepository categoriaRepository;

	public CategoriaService(CategoriaRepository categoriaRepository) {
		super();
		this.categoriaRepository = categoriaRepository;
	}
	
	public List<CategoriaResponseDTO> buscarTodos(){
		List<Categoria> categorias = categoriaRepository.findAll();
		
		return categorias.stream().map(this::toDTO).collect(Collectors.toList());
	}
	
	
	public CategoriaResponseDTO buscarPorId(Long id) {
		Categoria categoria = categoriaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Categoria não encontrado"));
		
		return toDTO(categoria);
	}
	
	public Categoria adicionarCategoria(CategoriaRequestDTO dto) {
		
		Categoria categoria = new Categoria(null, dto.nome());
		
		return categoriaRepository.save(categoria);
	}
	
	public Categoria atualizarCategoria(Long id, CategoriaRequestDTO dto) {
		try {
			Categoria categoria = categoriaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Categoria não encontrado"));
			updateCategoria(dto, categoria);
			return categoria;
		} catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Categoria não encotrado!");
        }
		
	}
	
	private void updateCategoria(CategoriaRequestDTO dto, Categoria categoria) {
		categoria.setNome(dto.nome());

	}
	
	public void deletarCategoria(Long id) {
		try {
			if(!categoriaRepository.existsById(id)) {
				throw new EntityNotFoundException("Categoria não encontrado");
			}
			categoriaRepository.deleteById(id);
			
		} catch (Exception e) {
			throw new RuntimeException("Exceção genérica para teste");
		}
	}
	
	public CategoriaResponseDTO toDTO(Categoria categoria) {
		return new CategoriaResponseDTO(categoria.getIdCategoria(), categoria.getNome());
	}
	
}
