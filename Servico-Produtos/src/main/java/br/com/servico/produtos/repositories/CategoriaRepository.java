package br.com.servico.produtos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.servico.produtos.entities.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

}
