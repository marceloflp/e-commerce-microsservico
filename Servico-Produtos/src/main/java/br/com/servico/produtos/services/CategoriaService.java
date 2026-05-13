package br.com.servico.produtos.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.servico.produtos.dtos.CategoriaRequestDTO;
import br.com.servico.produtos.dtos.CategoriaResponseDTO;
import br.com.servico.produtos.entities.Categoria;
import br.com.servico.produtos.repositories.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoriaService {
	
	private final Logger logger = LoggerFactory.getLogger(CategoriaService.class);

	private final CategoriaRepository categoriaRepository;

	public CategoriaService(CategoriaRepository categoriaRepository) {
		super();
		this.categoriaRepository = categoriaRepository;
	}
	
	public List<CategoriaResponseDTO> buscarTodos(){
		logger.info("Buscando todos as categorias cadastrados");
		List<Categoria> categorias = categoriaRepository.findAll();
		
		return categorias.stream().map(this::toDTO).collect(Collectors.toList());
	}
	
	
	public CategoriaResponseDTO buscarPorId(Long id) {
		
		logger.info("Buscando categoria de id {}", id);
		
		Categoria categoria = categoriaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Categoria não encontrado"));
		
		return toDTO(categoria);
	}
	
	public Categoria adicionarCategoria(CategoriaRequestDTO dto) {
		logger.info("Adicionando nova categoria...");
		
		Categoria categoria = new Categoria(null, dto.nome(), null);
		
		categoriaRepository.save(categoria);
		
		logger.info("Categoria cadastrada com sucesso!");
		
		return categoria;
	}
	
	public Categoria atualizarCategoria(Long id, CategoriaRequestDTO dto) {
		
		logger.info("Atualizando categoria. ID: {}", id);
		
		try {
			Categoria categoria = categoriaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Categoria não encontrado"));
			updateCategoria(dto, categoria);
			categoriaRepository.save(categoria);
			logger.info("Categoria de ID {} atualizado com sucesso!", id);
			return categoria;
		} catch (EntityNotFoundException e) {
			logger.error("Erro ao atualizar: Categoria não encontrada");
            throw new EntityNotFoundException("Categoria não encotrado!");
        }
		
	}
	
	private void updateCategoria(CategoriaRequestDTO dto, Categoria categoria) {
		categoria.setNome(dto.nome());

	}
	
	public void deletarCategoria(Long id) {
		logger.info("Deletando categoria. ID: {}", id);
		try {
			if(!categoriaRepository.existsById(id)) {
				throw new EntityNotFoundException("Categoria não encontrado");
			}
			categoriaRepository.deleteById(id);
			logger.info("Categoria deletada com sucesso!");
		} catch (Exception e) {
			logger.error("Erro ao deletar categoria: {}", e.toString());
			throw new RuntimeException("Exceção genérica para teste");
		}
	}
	
	public CategoriaResponseDTO toDTO(Categoria categoria) {
		return new CategoriaResponseDTO(categoria.getIdCategoria(), categoria.getNome());
	}
	
}
