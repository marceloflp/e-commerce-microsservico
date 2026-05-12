package br.com.servico.produtos.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.servico.produtos.dtos.ProdutoRequestDTO;
import br.com.servico.produtos.dtos.ProdutoResponseDTO;
import br.com.servico.produtos.entities.Produto;
import br.com.servico.produtos.repositories.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ProdutoService {

	private final ProdutoRepository produtoRepository;

	public ProdutoService(ProdutoRepository produtoRepository) {
		super();
		this.produtoRepository = produtoRepository;
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
		
		Produto produto = new Produto(null, dto.nome(), dto.descricao(), dto.preco());
		
		return produtoRepository.save(produto);
	}
	
	public Produto atualizarProduto(Long id, ProdutoRequestDTO dto) {
		try {
			Produto produto = produtoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));
			updateProduto(dto, produto);
			return produto;
		} catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Produto não encotrado!");
        }
		
	}
	
	private void updateProduto(ProdutoRequestDTO dto, Produto produto) {
		produto.setNomeProduto(dto.nome());
		produto.setDescricao(dto.descricao());
		produto.setPreco(dto.preco());
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
		return new ProdutoResponseDTO(produto.getIdProduto(), produto.getNomeProduto(), produto.getDescricao(), produto.getPreco());
	}
	
}
