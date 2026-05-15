package com.servico.pedidos.dtos;

import java.math.BigDecimal;

public record ProdutoResponseDTO( Long id,
	    String nome,
	    String descricao,
	    BigDecimal preco,
	    Integer estoque) {

}
