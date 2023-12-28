package cesar.montaldi.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cesar.montaldi.lojavirtual.model.PessoaJuridica;

@Repository
public interface PessoaJuridicaRepository extends CrudRepository<PessoaJuridica, Long> {
	
	/*Faz busca no banco para validar se o CNPJ já esta cadastrado*/
	@Query(value = "select pj from PessoaJuridica pj where pj.cnpj = ?1")
	public PessoaJuridica existeCnpjCadastrado(String cnpj);
	
	/*Faz busca no banco pelo CNPJ*/
	@Query(value = "select pj from PessoaJuridica pj where pj.cnpj = ?1")
	public List<PessoaJuridica> consultaPorCnpj(String cnpj);
	
	/*Faz busca no banco pelo nome*/
	@Query(value = "select pj from PessoaJuridica pj where upper(trim(pj.nome)) like %?1%")
	public List<PessoaJuridica> consultaPorNomePJ(String nome);
	
	/*Faz busca no banco para validar se a Inscrição Estadual já esta cadastrada*/
	@Query(value = "select pj from PessoaJuridica pj where pj.inscricaoEstadual = ?1")
	public PessoaJuridica existeInscricaoEstadualCadastrada(String inscricaoEstadual);

}
