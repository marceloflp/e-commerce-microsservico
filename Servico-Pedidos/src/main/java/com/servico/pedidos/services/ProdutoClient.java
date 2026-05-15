package com.servico.pedidos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.servico.pedidos.dtos.ProdutoResponseDTO;

@Service
public class ProdutoClient {

    @Autowired
    private WebClient webClient;

    public ProdutoResponseDTO buscarProduto(Long id) {

        return webClient
                .get()
                .uri("http://localhost:8080/produtos/buscarPorId/id/" + id)
                .retrieve()
                .bodyToMono(ProdutoResponseDTO.class)
                .block();
    }
}
