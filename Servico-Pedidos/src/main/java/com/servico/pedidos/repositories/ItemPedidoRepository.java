package com.servico.pedidos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.servico.pedidos.entities.ItemPedido;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long>{

}
