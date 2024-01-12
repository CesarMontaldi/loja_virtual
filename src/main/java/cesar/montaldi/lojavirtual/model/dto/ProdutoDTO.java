package cesar.montaldi.lojavirtual.model.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cesar.montaldi.lojavirtual.model.CategoriaProduto;
import cesar.montaldi.lojavirtual.model.MarcaProduto;
import cesar.montaldi.lojavirtual.model.PessoaJuridica;

public class ProdutoDTO {
	
	private Long id;
	
	private String nome;
	
	private String descricao;
	
	private Double peso;
	
	private Double largura;

	private Double altura;
	
	private Double profundidade;
	
	private BigDecimal valorVenda = BigDecimal.ZERO;
	
	private Integer quantidadeEstoque = 0;
	
	private Integer quantidadeAlertaEstoque = 0;
	
	private String linkYoutube;
	
	private Boolean alertaQuantidadeEstoque = Boolean.FALSE;
	
	private Integer quantidadeClique = 0;
	
	private PessoaJuridica empresa;
	
	private CategoriaProduto categoriaProduto;
	
	private MarcaProduto marcaProduto;
	
	private String categoriaDoProduto;
	
	private String marcaDoProduto;

	private List<ImagemProdutoDTO> imagens = new ArrayList<ImagemProdutoDTO>();
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	public Double getLargura() {
		return largura;
	}

	public void setLargura(Double largura) {
		this.largura = largura;
	}

	public Double getAltura() {
		return altura;
	}

	public void setAltura(Double altura) {
		this.altura = altura;
	}

	public Double getProfundidade() {
		return profundidade;
	}

	public void setProfundidade(Double profundidade) {
		this.profundidade = profundidade;
	}

	public BigDecimal getValorVenda() {
		return valorVenda;
	}

	public void setValorVenda(BigDecimal valorVenda) {
		this.valorVenda = valorVenda;
	}

	public Integer getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(Integer quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}

	public List<ImagemProdutoDTO> getImagens() {
		return imagens;
	}

	public void setImagens(List<ImagemProdutoDTO> imagens) {
		this.imagens = imagens;
	}

	public PessoaJuridica getEmpresa() {
		return empresa;
	}

	public void setEmpresa(PessoaJuridica empresa) {
		this.empresa = empresa;
	}
		

	public CategoriaProduto getCategoriaProduto() {
		return categoriaProduto;
	}

	public void setCategoriaProduto(CategoriaProduto categoriaProduto) {
		this.categoriaProduto = categoriaProduto;
	}

	public MarcaProduto getMarcaProduto() {
		return marcaProduto;
	}
	
	public void setCategoriaDoProduto(String categoriaDoProduto) {
		this.categoriaDoProduto = categoriaDoProduto;
	}
	
	public String getCategoriaDoProduto() {
		return categoriaDoProduto;
	}
	
	public void setMarcaDoProduto(String marcaDoProduto) {
		this.marcaDoProduto = marcaDoProduto;
	}
	
	public String getMarcaDoProduto() {
		return marcaDoProduto;
	}

	public void setMarcaProduto(MarcaProduto marcaProduto) {
		this.marcaProduto = marcaProduto;
	}

	public Integer getQuantidadeAlertaEstoque() {
		return quantidadeAlertaEstoque;
	}

	public void setQuantidadeAlertaEstoque(Integer quantidadeAlertaEstoque) {
		this.quantidadeAlertaEstoque = quantidadeAlertaEstoque;
	}

	public String getLinkYoutube() {
		return linkYoutube;
	}

	public void setLinkYoutube(String linkYoutube) {
		this.linkYoutube = linkYoutube;
	}

	public Boolean getAlertaQuantidadeEstoque() {
		return alertaQuantidadeEstoque;
	}

	public void setAlertaQuantidadeEstoque(Boolean alertaQuantidadeEstoque) {
		this.alertaQuantidadeEstoque = alertaQuantidadeEstoque;
	}

	public Integer getQuantidadeClique() {
		return quantidadeClique;
	}

	public void setQuantidadeClique(Integer quantidadeClique) {
		this.quantidadeClique = quantidadeClique;
	}
	
}
