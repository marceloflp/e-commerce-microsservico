package com.servico.pedidos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.servico.pedidos.entities.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long>{

}
