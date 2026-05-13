package br.com.servico.produtos.dtos;

public record ProdutoRequestDTO(String nome, String descricao, Double preco, Integer quantidade, Long idCategoria) {

}
