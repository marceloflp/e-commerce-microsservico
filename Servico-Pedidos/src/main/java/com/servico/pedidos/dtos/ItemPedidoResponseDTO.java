package com.servico.pedidos.dtos;

import java.math.BigDecimal;

public record ItemPedidoResponseDTO(Long idItemPedido, Long idProduto, String nomeProduto, BigDecimal precoProduto, Integer quantidade) {

}
