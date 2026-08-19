package com.servico.pedidos.exceptions;

public class ProdutoIndisponivelException extends RuntimeException{
    
    public ProdutoIndisponivelException(String msg){
        super(msg);
    }
}
