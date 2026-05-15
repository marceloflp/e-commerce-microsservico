package br.com.servico.produtos.dtos;

import java.math.BigDecimal;

public record ProdutoRequestDTO(String nome, String descricao, BigDecimal preco, Integer estoque, Long idCategoria) {

}
