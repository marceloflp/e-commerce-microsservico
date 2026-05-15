package br.com.servico.produtos.dtos;

import java.math.BigDecimal;

public record ProdutoResponseDTO(Long id, String nome, String descricao, BigDecimal preco, Integer estoque, CategoriaResponseDTO categoria) {

}
