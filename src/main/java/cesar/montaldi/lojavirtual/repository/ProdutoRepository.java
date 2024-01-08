package cesar.montaldi.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cesar.montaldi.lojavirtual.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>{
	
	@Query(nativeQuery = true, value = "SELECT count (1) > 0 from produto where upper(nome) = upper(trim(?1))")
	public boolean existeProduto(String produto);

	@Query("select p from Produto p where upper(p.nome) like %?1%")
	List<Produto> buscarProdutoPorNome(String nome);
	
	@Query("select p from Produto p where upper(trim(p.nome)) like %?1% and p.empresa.id = ?2")
	List<Produto> buscarProdutoPorNome(String nome, Long idEmpresa);

}
