package cesar.montaldi.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cesar.montaldi.lojavirtual.model.MarcaProduto;

@Repository
@Transactional
public interface MarcaProdutoRepository extends JpaRepository<MarcaProduto, Long>{
	
	@Query("select m from MarcaProduto m where upper(trim(m.nomeDescricao)) like %?1%")
	List<MarcaProduto> buscarMarcaDescricao(String nomeDescricao);
	
}
