package br.com.servico.produtos.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_categorias")
public class Categoria implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idCategoria;
	private String nome;
	
	@OneToMany(mappedBy = "categoria")
	private List<Produto> produtos = new ArrayList<>();
	
	
	public Categoria() {}
	
	public Categoria(Long idCategoria, String nome, List<Produto> produtos) {
		super();
		this.idCategoria = idCategoria;
		this.nome = nome;
		this.produtos = produtos;
	}
	
	public Long getIdCategoria() {
		return idCategoria;
	}
	public void setIdCategoria(Long idCategoria) {
		this.idCategoria = idCategoria;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	@Override
	public int hashCode() {
		return Objects.hash(idCategoria);
	}
	
	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Categoria other = (Categoria) obj;
		return Objects.equals(idCategoria, other.idCategoria);
	}

}
