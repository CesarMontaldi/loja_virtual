package cesar.montaldi.lojavirtual.repository;

import java.util.List;

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
	
	/*Faz busca no banco pelo CPF*/
	@Query(value = "select pf from PessoaFisica pf where pf.cpf = ?1")
	public List<PessoaFisica> consultaPorCpf(String cpf);
	
	/*Faz busca no banco pelo nome*/
	@Query(value = "select pf from PessoaFisica pf where upper(trim(pf.nome)) like %?1%")
	public List<PessoaFisica> consultaPorNomePF(String nome);
	
}
