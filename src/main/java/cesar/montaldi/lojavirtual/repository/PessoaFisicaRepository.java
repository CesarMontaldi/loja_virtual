package cesar.montaldi.lojavirtual.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cesar.montaldi.lojavirtual.model.PessoaFisica;
import cesar.montaldi.lojavirtual.model.PessoaJuridica;

@Repository
public interface PessoaFisicaRepository extends CrudRepository<PessoaFisica, Long> {
	
	/*Faz busca no banco para validar se o CPF já esta cadastrado*/
	@Query(value = "select pf from PessoaFisica pf where pf.cpf = ?1")
	public PessoaFisica existeCpfCadastrado(String cpf);
	
}
