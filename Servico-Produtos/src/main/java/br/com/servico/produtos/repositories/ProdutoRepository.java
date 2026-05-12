package br.com.servico.produtos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.servico.produtos.entities.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long>{

}
