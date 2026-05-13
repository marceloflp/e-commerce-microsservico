package br.com.servico.produtos.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.servico.produtos.dtos.CategoriaResponseDTO;
import br.com.servico.produtos.dtos.ProdutoRequestDTO;
import br.com.servico.produtos.dtos.ProdutoResponseDTO;
import br.com.servico.produtos.entities.Categoria;
import br.com.servico.produtos.entities.Produto;
import br.com.servico.produtos.repositories.CategoriaRepository;
import br.com.servico.produtos.repositories.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ProdutoService {

	private final ProdutoRepository produtoRepository;
	private final CategoriaRepository categoriasRepository;

	public ProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
		super();
		this.produtoRepository = produtoRepository;
		this.categoriasRepository = categoriaRepository;
	}
	
	public List<ProdutoResponseDTO> buscarTodos(){
		List<Produto> produtos = produtoRepository.findAll();
		
		return produtos.stream().map(this::toDTO).collect(Collectors.toList());
	}
	
	
	public ProdutoResponseDTO buscarPorId(Long id) {
		Produto produto = produtoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));
		
		return toDTO(produto);
	}
	
	public Produto adicionarProduto(ProdutoRequestDTO dto) {
		Categoria categoria = categoriasRepository.findById(dto.idCategoria())
				.orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
		
		Produto produto = new Produto(null, dto.nome(), dto.descricao(), dto.preco(), categoria);
		
		return produtoRepository.save(produto);
	}
	
	public Produto atualizarProduto(Long id, ProdutoRequestDTO dto) {
		try {
			Produto produto = produtoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));
			updateProduto(dto, produto);
			return produtoRepository.save(produto);
		} catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Produto não encotrado!");
        }
		
	}
	
	private void updateProduto(ProdutoRequestDTO dto, Produto produto) {

		Categoria categoria = categoriasRepository.findById(dto.idCategoria())
				.orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
		
		produto.setNomeProduto(dto.nome());
		produto.setDescricao(dto.descricao());
		produto.setPreco(dto.preco());
		produto.setCategoria(categoria);
	}
	
	public void deletarProduto(Long id) {
		try {
			if(!produtoRepository.existsById(id)) {
				throw new EntityNotFoundException("Produto não encontrado");
			}
			produtoRepository.deleteById(id);
			
		} catch (Exception e) {
			throw new RuntimeException("Exceção genérica para teste");
		}
	}
	
	public ProdutoResponseDTO toDTO(Produto produto) {
		CategoriaResponseDTO categoriaDTO = new CategoriaResponseDTO(produto.getCategoria().getIdCategoria(), produto.getCategoria().getNome()); 
		return new ProdutoResponseDTO(produto.getIdProduto(), produto.getNomeProduto(), produto.getDescricao(), produto.getPreco(), categoriaDTO);
	}
	
}
