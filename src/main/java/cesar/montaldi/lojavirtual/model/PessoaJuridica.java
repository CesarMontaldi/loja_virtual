package cesar.montaldi.lojavirtual.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.validator.constraints.br.CNPJ;

@Entity
@Table(name= "pessoa_juridica")
@PrimaryKeyJoinColumn(name = "id")
public class PessoaJuridica  extends Pessoa {

	private static final long serialVersionUID = 1L;
	
	@CNPJ(message = "CNPJ está inválido")
	@Column(nullable = false)
	private String cnpj;
	
	@Column(nullable = false)
	private String inscricaoEstadual;

	private String inscricaoMunicipal;
	
	@Column(nullable = false)
	private String nomeFantasia;
	
	@Column(nullable = false)
	private String razaoSocial;

	private String categoria;
	
	public String getCnpj() {
		return cnpj;
	}
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	public String getinscricaoEstadual() {
		return inscricaoEstadual;
	}
	public void setinscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}
	public String getinscricaoMunicipal() {
		return inscricaoMunicipal;
	}
	public void setinscricaoMunicipal(String inscricaoMunicipal) {
		this.inscricaoMunicipal = inscricaoMunicipal;
	}
	public String getNomeFantasia() {
		return nomeFantasia;
	}
	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}
	public String getRazaoSocial() {
		return razaoSocial;
	}
	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	
	
}
