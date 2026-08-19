package com.servico.pedidos.services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.servico.pedidos.dtos.ProdutoResponseDTO;
import com.servico.pedidos.exceptions.ProdutoIndisponivelException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class ProdutoClient {

    @Autowired
    private WebClient webClient;

    @CircuitBreaker(name = "produtoService", fallbackMethod = "fallback")
    public ProdutoResponseDTO buscarProduto(Long id) {

        return webClient
                .get()
                .uri("http://localhost:8080/api/produtos/buscarPorId/id/" + id)
                .retrieve()
                .bodyToMono(ProdutoResponseDTO.class)
                .block();
    }

    public ProdutoResponseDTO fallback(Long id, Throwable throwable) {
        throw new ProdutoIndisponivelException("ERRO: Produto Indisponível!");
    }

}
