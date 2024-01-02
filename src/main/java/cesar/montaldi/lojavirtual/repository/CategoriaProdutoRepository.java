package cesar.montaldi.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cesar.montaldi.lojavirtual.model.Acesso;
import cesar.montaldi.lojavirtual.model.CategoriaProduto;

@Repository
public interface CategoriaProdutoRepository extends JpaRepository<CategoriaProduto, Long>{
	
	@Query(nativeQuery = true, value = "SELECT count (1) > 0 from categoria_produto where upper(nome_descricao) = ?1")
	public boolean existeCategoria(String nomeCategoria);
	
	@Query("select cat from CategoriaProduto cat where upper(trim(cat.nomeDescricao)) like %?1%")
	List<CategoriaProduto> buscarCategoriaPorDescricao(String nomeDescricao);

}
