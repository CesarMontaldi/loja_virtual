package cesar.montaldi.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cesar.montaldi.lojavirtual.model.ContaPagar;

@Repository
@Transactional
public interface ContaPagarRepository extends JpaRepository<ContaPagar, Long>{
	
	@Query("SELECT c FROM ContaPagar c WHERE upper(trim(c.descricao)) like %?1%")
	List<ContaPagar> buscarContaDescricao(String desc);
	
	@Query("SELECT c FROM ContaPagar c WHERE c.pessoa.id = ?1")
	List<ContaPagar> buscarContaPorPessoa(Long idPessoa);

	@Query("SELECT c FROM ContaPagar c WHERE c.pessoa_fornecedor.id = ?1")
	List<ContaPagar> buscarContaPorFornecedor(Long idFornecedor);
	
	@Query("SELECT c FROM ContaPagar c WHERE c.empresa.id = ?1")
	List<ContaPagar> buscarContaPorEmpresa(Long idEmpresa);
}
