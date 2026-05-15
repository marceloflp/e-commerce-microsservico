package com.servico.pedidos.dtos;

import java.math.BigDecimal;

public record ProdutoResponseDTO(Long idProduto, String nomeProduto, BigDecimal preco) {

}
