package com.servico.pedidos.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.servico.pedidos.enums.Status;

public record PedidoRequestDTO(String nomeCliente, @JsonFormat(pattern = "dd/MM/yyyy") LocalDate dataPedido, Status status, BigDecimal valorTotal) {

}
