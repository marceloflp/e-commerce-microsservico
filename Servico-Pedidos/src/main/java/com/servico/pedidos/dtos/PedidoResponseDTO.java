package com.servico.pedidos.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.servico.pedidos.enums.Status;

public record PedidoResponseDTO(Long idPedido, String nomeCliente, LocalDate dataPedido, Status status,  
		BigDecimal valorTotal, List<ItemPedidoResponseDTO> itens, String emailNotificacao) {

}
