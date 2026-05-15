package com.servico.pedidos.events;

import java.math.BigDecimal;
import java.util.List;

public record PedidoCriadoEvent(Long idProduto, String nomeCliente, BigDecimal valorTotal, List<ItemPedidoEventDTO> itens) {

}
