package br.com.servico.produtos.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.servico.produtos.dtos.CategoriaResponseDTO;
import br.com.servico.produtos.dtos.ProdutoRequestDTO;
import br.com.servico.produtos.dtos.ProdutoResponseDTO;
import br.com.servico.produtos.entities.Categoria;
import br.com.servico.produtos.entities.Produto;
import br.com.servico.produtos.producer.ProdutoProducer;
import br.com.servico.produtos.repositories.CategoriaRepository;
import br.com.servico.produtos.repositories.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ProdutoService {
	
	private static final Logger logger = LoggerFactory.getLogger(ProdutoProducer.class);

	private final ProdutoRepository produtoRepository;
	private final CategoriaRepository categoriasRepository;
	private final ProdutoProducer produtoProducer;

	public ProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository, ProdutoProducer produtoProducer) {
		super();
		this.produtoRepository = produtoRepository;
		this.categoriasRepository = categoriaRepository;
		this.produtoProducer = produtoProducer;
	}
	
	public List<ProdutoResponseDTO> buscarTodos(){
		logger.info("Buscando todos os produtos cadastrados");
		List<Produto> produtos = produtoRepository.findAll();
		
		return produtos.stream().map(this::toDTO).collect(Collectors.toList());
	}
	
	
	public ProdutoResponseDTO buscarPorId(Long id) {
		
		logger.info("Buscando produto de id {}", id);
		
		Produto produto = produtoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));
		
		return toDTO(produto);
	}
	
	public Produto adicionarProduto(ProdutoRequestDTO dto) {
		
		logger.info("Adicionando novo produto...");
		
		Categoria categoria = categoriasRepository.findById(dto.idCategoria())
				.orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
		
		Produto produto = new Produto(null, dto.nome(), dto.descricao(), dto.preco(), categoria);
	
		
		produtoRepository.save(produto);
		
		CategoriaResponseDTO categoriaDTO = new CategoriaResponseDTO(produto.getCategoria().getIdCategoria(), produto.getCategoria().getNome());
		
		ProdutoResponseDTO produtoDTO = new ProdutoResponseDTO(produto.getIdProduto(), produto.getNomeProduto(), produto.getDescricao(), produto.getPreco(), categoriaDTO);
		
		logger.info("Produto criado: {}", produtoDTO);
		
		produtoProducer.enviarProdutoCriado(produtoDTO);
		
		return produto;
	}
	
	public Produto atualizarProduto(Long id, ProdutoRequestDTO dto) {
		
		logger.info("Atualizando produto. ID: {}", id);
		
		try {
			Produto produto = produtoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));
			updateProduto(dto, produto);
			produtoRepository.save(produto);
			logger.info("Produto de ID {} atualizado com sucesso!", id);
			
			return produto;
		} catch (EntityNotFoundException e) {
			logger.error("Erro ao atualizar: Produto não encontrado");
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
		logger.info("Deletando produto. ID: {}", id);
		try {
			if(!produtoRepository.existsById(id)) {
				throw new EntityNotFoundException("Produto não encontrado");
			}
			produtoRepository.deleteById(id);
			logger.info("Categoria deletada com sucesso!");
		} catch (Exception e) {
			logger.error("Erro ao deletar produto: {}", e.toString());
			throw new RuntimeException("Exceção genérica para teste");
		}
	}
	
	public ProdutoResponseDTO toDTO(Produto produto) {
		CategoriaResponseDTO categoriaDTO = new CategoriaResponseDTO(produto.getCategoria().getIdCategoria(), produto.getCategoria().getNome()); 
		return new ProdutoResponseDTO(produto.getIdProduto(), produto.getNomeProduto(), produto.getDescricao(), produto.getPreco(), categoriaDTO);
	}
	
}
