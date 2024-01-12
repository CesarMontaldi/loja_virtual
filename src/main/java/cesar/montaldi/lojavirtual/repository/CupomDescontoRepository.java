package cesar.montaldi.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cesar.montaldi.lojavirtual.model.CupomDesconto;

public interface CupomDescontoRepository extends JpaRepository<CupomDesconto, Long> {
	
	@Query(value = "select c from CupomDesconto c where c.empresa.id = ?1")
	public List<CupomDesconto> cupomDescontoPorEmpresa(Long idEmpresa);
}
