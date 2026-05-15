package com.servico.pedidos.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.servico.pedidos.enums.Status;

public record PedidoRequestDTO(String nomeCliente, LocalDate dataPedido, Status status, BigDecimal valorTotal) {

}
