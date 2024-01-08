package cesar.montaldi.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cesar.montaldi.lojavirtual.model.AvaliacaoProduto;

@Repository
@Transactional
public interface AvaliacaoProdutoRepository extends JpaRepository<AvaliacaoProduto, Long> {
	
	@Query(value = "select  a from AvaliacaoProduto a where a.produto.id = ?1")
	List<AvaliacaoProduto> avaliacaoProduto(Long idProduto);
	
	@Query(value = "select  a from AvaliacaoProduto a where a.produto.id = ?1 and a.pessoa.id = ?2")
	List<AvaliacaoProduto> avaliacaoProdutoPessoa(Long idPessoa, Long IdPessoa);
	
	@Query(value = "select  a from AvaliacaoProduto a where a.pessoa.id = ?1")
	List<AvaliacaoProduto> avaliacaoPessoa(Long idPessoa);
}
