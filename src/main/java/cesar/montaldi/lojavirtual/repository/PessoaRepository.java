package cesar.montaldi.lojavirtual.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cesar.montaldi.lojavirtual.model.PessoaJuridica;

@Repository
public interface PessoaRepository extends CrudRepository<PessoaJuridica, Long> {
	
	/*Faz busca no banco para validar se o CNPJ já esta cadastrado*/
	@Query(value = "select pj from PessoaJuridica pj where pj.cnpj = ?1")
	public PessoaJuridica existeCnpjCadastrado(String cnpj);
	
	/*Faz busca no banco para validar se a Inscrição Estadual já esta cadastrada*/
	@Query(value = "select pj from PessoaJuridica pj where pj.inscricaoEstadual = ?1")
	public PessoaJuridica existeInscricaoEstadualCadastrada(String inscricaoEstadual);

}
