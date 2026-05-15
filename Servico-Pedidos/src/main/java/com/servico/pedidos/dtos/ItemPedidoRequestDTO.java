package com.servico.pedidos.dtos;

import java.math.BigDecimal;

public record ItemPedidoRequestDTO(Long idProduto, String nomeProduto, BigDecimal precoProduto, Integer quantidade) {

}
