package cesar.montaldi.lojavirtual.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cesar.montaldi.lojavirtual.ExceptionLojaVirtual;
import cesar.montaldi.lojavirtual.enums.TipoPessoa;
import cesar.montaldi.lojavirtual.model.Endereco;
import cesar.montaldi.lojavirtual.model.PessoaFisica;
import cesar.montaldi.lojavirtual.model.PessoaJuridica;
import cesar.montaldi.lojavirtual.model.dto.CepDTO;
import cesar.montaldi.lojavirtual.model.dto.ConsultaCnpjDto;
import cesar.montaldi.lojavirtual.repository.EnderecoRepository;
import cesar.montaldi.lojavirtual.repository.PessoaFisicaRepository;
import cesar.montaldi.lojavirtual.repository.PessoaJuridicaRepository;
import cesar.montaldi.lojavirtual.service.PessoaUserService;
import cesar.montaldi.lojavirtual.service.ServiceContagemAcessoApi;
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
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private ServiceContagemAcessoApi serviceContagemAcessoApi;
	
	@ResponseBody
	@GetMapping(value = "/consultaNomePF/{nome}")
	public ResponseEntity<List<PessoaFisica>> consultaNomePF(@PathVariable("nome") String nome) {
		
		List<PessoaFisica> fisicas = pessoaFisicaRepository.consultaPorNomePF(nome.trim().toUpperCase());
		
		serviceContagemAcessoApi.atualizaAcessoEndPointPF();
		
		return new ResponseEntity<List<PessoaFisica>>(fisicas, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/consultaPorCpf/{cpf}")
	public ResponseEntity<List<PessoaFisica>> consultaPorCpf(@PathVariable("cpf") String cpf) {
		
		List<PessoaFisica> fisicas = pessoaFisicaRepository.consultaPorCpf(cpf);
		
		return new ResponseEntity<List<PessoaFisica>>(fisicas, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/consultaNomePJ/{nome}")
	public ResponseEntity<List<PessoaJuridica>> consultaNomePJ(@PathVariable("nome") String nome) {
		
		List<PessoaJuridica> juridicas = pessoaJuridicaRepository.consultaPorNomePJ(nome.trim().toUpperCase());
		
		return new ResponseEntity<List<PessoaJuridica>>(juridicas, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/consultaPorCnpj/{cnpj}")
	public ResponseEntity<List<PessoaJuridica>> consultaPorCnpj(@PathVariable("cnpj") String cnpj) {
		
		List<PessoaJuridica> juridicas = pessoaJuridicaRepository.consultaPorCnpj(cnpj);
		
		return new ResponseEntity<List<PessoaJuridica>>(juridicas, HttpStatus.OK);
	}
	
	
	@ResponseBody
	@GetMapping(value = "/consultaCep/{cep}")
	public ResponseEntity<CepDTO> consultaCep(@PathVariable("cep") String cep) {

		return new ResponseEntity<CepDTO>(pessoaUserService.consultaCep(cep), HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/consultaCnpjReceitaWs/{cnpj}")
	public ResponseEntity<ConsultaCnpjDto> consultaCnpjReceitaWS(@PathVariable("cnpj") String cnpj) {

		return new ResponseEntity<ConsultaCnpjDto>(pessoaUserService.consultaCnpjReceitaWS(cnpj), HttpStatus.OK);
	}
	
	@ResponseBody
	@PostMapping(value = "/salvarPj")
	public ResponseEntity<PessoaJuridica> salvarPj(@RequestBody @Valid PessoaJuridica pessoaJuridica) throws ExceptionLojaVirtual {
		
		if (pessoaJuridica == null) {
			throw new ExceptionLojaVirtual("Pessoa juridica não pode ser NULL");
		}
		
		if (pessoaJuridica.getTipoPessoa() == null) {
			throw new ExceptionLojaVirtual("Informe o tipo Jurídico ou Fornecedor da Loja");
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
		
		if (pessoaJuridica.getId() == null || pessoaJuridica.getId() <= 0) {
			for (int p = 0; p < pessoaJuridica.getEnderecos().size(); p++) {
				
				CepDTO cepDTO = pessoaUserService.consultaCep(pessoaJuridica.getEnderecos().get(p).getCep());
				
				pessoaJuridica.getEnderecos().get(p).setBairro(cepDTO.getBairro());
				pessoaJuridica.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
				pessoaJuridica.getEnderecos().get(p).setComplemento(cepDTO.getComplemento());
				pessoaJuridica.getEnderecos().get(p).setLogradouro(cepDTO.getLogradouro());
				pessoaJuridica.getEnderecos().get(p).setUf(cepDTO.getUf());
			}
		}else {
			for (int p = 0; p < pessoaJuridica.getEnderecos().size(); p++) {
				
				Endereco enderecoTemp = enderecoRepository.findById(pessoaJuridica.getEnderecos().get(p).getId()).get();
				
				if (!enderecoTemp.getCep().equals(pessoaJuridica.getEnderecos().get(p).getCep())) {
					
					CepDTO cepDTO = pessoaUserService.consultaCep(pessoaJuridica.getEnderecos().get(p).getCep());
					
					pessoaJuridica.getEnderecos().get(p).setBairro(cepDTO.getBairro());
					pessoaJuridica.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
					pessoaJuridica.getEnderecos().get(p).setComplemento(cepDTO.getComplemento());
					pessoaJuridica.getEnderecos().get(p).setLogradouro(cepDTO.getLogradouro());
					pessoaJuridica.getEnderecos().get(p).setUf(cepDTO.getUf());				
				}
			}
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
		
		if (pessoaFisica.getTipoPessoa() == null) {
			pessoaFisica.setTipoPessoa(TipoPessoa.FISICA.name());
		}
		
		if (pessoaFisica.getId() == null && pessoaFisicaRepository.existeCpfCadastrado(pessoaFisica.getCpf()) != null) {
			throw new ExceptionLojaVirtual("Já existe CPF cadastrado com o número: " + pessoaFisica.getCpf());
		}
	
		if (!ValidaCPF.isCPF(pessoaFisica.getCpf())) {
			throw new ExceptionLojaVirtual("Cpf: " + pessoaFisica.getCpf() + " está inválido.");
		}

		if (pessoaFisica.getId() == null || pessoaFisica.getId() <= 0) {
			for (int p = 0; p < pessoaFisica.getEnderecos().size(); p++) {
				
				CepDTO cepDTO = pessoaUserService.consultaCep(pessoaFisica.getEnderecos().get(p).getCep());
				
				pessoaFisica.getEnderecos().get(p).setBairro(cepDTO.getBairro());
				pessoaFisica.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
				pessoaFisica.getEnderecos().get(p).setComplemento(cepDTO.getComplemento());
				pessoaFisica.getEnderecos().get(p).setLogradouro(cepDTO.getLogradouro());
				pessoaFisica.getEnderecos().get(p).setUf(cepDTO.getUf());
			}
		}else {
			for (int p = 0; p < pessoaFisica.getEnderecos().size(); p++) {
				
				Endereco enderecoTemp = enderecoRepository.findById(pessoaFisica.getEnderecos().get(p).getId()).get();
				
				if (!enderecoTemp.getCep().equals(pessoaFisica.getEnderecos().get(p).getCep())) {
					
					CepDTO cepDTO = pessoaUserService.consultaCep(pessoaFisica.getEnderecos().get(p).getCep());
					
					pessoaFisica.getEnderecos().get(p).setBairro(cepDTO.getBairro());
					pessoaFisica.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
					pessoaFisica.getEnderecos().get(p).setComplemento(cepDTO.getComplemento());
					pessoaFisica.getEnderecos().get(p).setLogradouro(cepDTO.getLogradouro());
					pessoaFisica.getEnderecos().get(p).setUf(cepDTO.getUf());
				}
			}
		}
		
		pessoaFisica = pessoaUserService.salvarPessoaFisica(pessoaFisica);
		
		return new ResponseEntity<PessoaFisica>(pessoaFisica, HttpStatus.OK);
	}
}
