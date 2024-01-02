package cesar.montaldi.lojavirtual.model.dto;

import java.io.Serializable;

public class CategoriaProdutoDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String nomeDescricao;
	private String empresa;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNomeDescricao() {
		return nomeDescricao;
	}
	public void setNomeDescricao(String nomeDescricao) {
		this.nomeDescricao = nomeDescricao;
	}
	public String getEmpresa() {
		return empresa;
	}
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	
}
