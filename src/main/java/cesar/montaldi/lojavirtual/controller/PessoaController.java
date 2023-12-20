package cesar.montaldi.lojavirtual.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cesar.montaldi.lojavirtual.ExceptionLojaVirtual;
import cesar.montaldi.lojavirtual.model.PessoaFisica;
import cesar.montaldi.lojavirtual.model.PessoaJuridica;
import cesar.montaldi.lojavirtual.repository.PessoaFisicaRepository;
import cesar.montaldi.lojavirtual.repository.PessoaJuridicaRepository;
import cesar.montaldi.lojavirtual.service.PessoaUserService;
import cesar.montaldi.lojavirtual.util.ValidaCNPJ;
import cesar.montaldi.lojavirtual.util.ValidaCPF;

@RestController
public class PessoaController {
	
	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;
	
	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;
	
	@Autowired
	private PessoaUserService pessoaUserService;

	
	@ResponseBody
	@PostMapping(value = "/salvarPj")
	public ResponseEntity<PessoaJuridica> salvarPj(@RequestBody PessoaJuridica pessoaJuridica) throws ExceptionLojaVirtual {
		
		if (pessoaJuridica == null) {
			throw new ExceptionLojaVirtual("Pessoa juridica não pode ser NULL");
		}
		
		if (pessoaJuridica.getId() == null && pessoaJuridicaRepository.existeCnpjCadastrado(pessoaJuridica.getCnpj()) != null) {
			throw new ExceptionLojaVirtual("Já existe CNPJ cadastrado com o número: " + pessoaJuridica.getCnpj());
		}
		
		if (pessoaJuridica.getId() == null && pessoaJuridicaRepository.existeInscricaoEstadualCadastrada(pessoaJuridica.getinscricaoEstadual()) != null) {
			throw new ExceptionLojaVirtual("Já existe Inscrição Estadual cadastrada com o número: " + pessoaJuridica.getinscricaoEstadual());
		}
		
		if (!ValidaCNPJ.isCNPJ(pessoaJuridica.getCnpj())) {
			throw new ExceptionLojaVirtual("Cnpj: " + pessoaJuridica.getCnpj() + " está inválido.");
		}
		
		pessoaJuridica = pessoaUserService.salvarPessoaJuridica(pessoaJuridica);
		
		return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);
	}
	
	@ResponseBody
	@PostMapping(value = "/salvarPf")
	public ResponseEntity<PessoaFisica> salvarPf(@RequestBody PessoaFisica pessoaFisica) throws ExceptionLojaVirtual {
		
		if (pessoaFisica == null) {
			throw new ExceptionLojaVirtual("Pessoa fisica não pode ser NULL");
		}
		
		if (pessoaFisica.getId() == null && pessoaFisicaRepository.existeCpfCadastrado(pessoaFisica.getCpf()) != null) {
			throw new ExceptionLojaVirtual("Já existe CPF cadastrado com o número: " + pessoaFisica.getCpf());
		}
	
		if (!ValidaCPF.isCPF(pessoaFisica.getCpf())) {
			throw new ExceptionLojaVirtual("Cpf: " + pessoaFisica.getCpf() + " está inválido.");
		}
		
		pessoaFisica = pessoaUserService.salvarPessoaFisica(pessoaFisica);
		
		return new ResponseEntity<PessoaFisica>(pessoaFisica, HttpStatus.OK);
	}
}
