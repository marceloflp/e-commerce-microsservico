package br.com.servico.produtos.events;

import java.math.BigDecimal;
import java.util.List;

public record PedidoCriadoEvent(Long idPedido, String nomeCliente, BigDecimal valorTotal, List<ItemPedidoEventDTO> itens) {

}
