package cesar.montaldi.lojavirtual.model.dto;

public class ItemVendaDTO {
	
	private Long id;
	
	private ProdutoDTO produto;
	
	private Double quantidade;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setProduto(ProdutoDTO produto) {
		this.produto = produto;
	}
	
	public ProdutoDTO getProduto() {
		return produto;
	}
	
	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}
	
	public Double getQuantidade() {
		return quantidade;
	}
}
