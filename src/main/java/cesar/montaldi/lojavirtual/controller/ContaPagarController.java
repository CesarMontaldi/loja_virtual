package cesar.montaldi.lojavirtual.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cesar.montaldi.lojavirtual.ExceptionLojaVirtual;
import cesar.montaldi.lojavirtual.model.ContaPagar;
import cesar.montaldi.lojavirtual.repository.ContaPagarRepository;

@Controller
@RestController 
public class ContaPagarController {
	
	
	@Autowired
	private ContaPagarRepository contaPagarRepository;

	@ResponseBody 
	@PostMapping(value = "/salvarContaPagar")
	public ResponseEntity<ContaPagar> salvarContaPagar(@RequestBody @Valid ContaPagar contapagar) throws ExceptionLojaVirtual {
		

		if (contapagar.getEmpresa() == null || contapagar.getEmpresa().getId() <= 0) {
			throw new ExceptionLojaVirtual("Empresa responsável deve ser informada.");
		}
		
		if (contapagar.getPessoa() == null || contapagar.getPessoa().getId() <= 0) {
			throw new ExceptionLojaVirtual("Pessoa responsável deve ser informada.");
		}
		
		if (contapagar.getPessoa_fornecedor() == null || contapagar.getPessoa_fornecedor().getId() <= 0) {
			throw new ExceptionLojaVirtual("Fornecedor responsável deve ser informado.");
		}
		
		if (contapagar.getId() == null) {
			List<ContaPagar> contasPagar = contaPagarRepository.buscarContaDescricao(contapagar.getDescricao().toUpperCase().trim());
			
			if (!contasPagar.isEmpty()) {
				throw new ExceptionLojaVirtual("Já existe uma Conta a Pagar com a mesma descrição.");
			}
		}
		
		ContaPagar contaPagarSalva = contaPagarRepository.save(contapagar);
		
		return new ResponseEntity<ContaPagar>(contaPagarSalva, HttpStatus.OK);
	}
	
	@ResponseBody
	@PostMapping(value = "/deleteContaPagar")
	public ResponseEntity<String> deleteContaPagar(@RequestBody ContaPagar contapagar) {
		
		if (!contaPagarRepository.findById(contapagar.getId()).isPresent()) {
			return new ResponseEntity<String>("Conta Pagar já foi removida.", HttpStatus.NOT_FOUND);
		}
		
		contaPagarRepository.deleteById(contapagar.getId());
		return new ResponseEntity<String>("Conta Pagar Removida", HttpStatus.OK);
	}
	
	@ResponseBody
	@DeleteMapping(value = "/deleteContaPagarId/{id}")
	public ResponseEntity<String> deleteContaPagarPorId(@PathVariable("id") Long id) {
		
		if (!contaPagarRepository.findById(id).isPresent()) {
			return new ResponseEntity<String>("Conta Pagar já foi removida.", HttpStatus.NOT_FOUND);
		}
		
		contaPagarRepository.deleteById(id);
		
		return new ResponseEntity<String>("Conta Pagar Removida", HttpStatus.OK);
	}
	
	@ResponseBody 
	@GetMapping(value = "/buscaContaPagarId/{id}")
	public ResponseEntity<ContaPagar> buscaContaPagarId(@PathVariable("id") Long id) throws ExceptionLojaVirtual {
		
		ContaPagar contapagar = contaPagarRepository.findById(id).orElse(null);

		if (contapagar == null) {
			throw new ExceptionLojaVirtual("Não foi possível encontrar a Conta Pagar com o código: " + id);
		}
		
		return new ResponseEntity<ContaPagar>(contapagar, HttpStatus.OK);
	}
	
	
	@ResponseBody
	@GetMapping(value = "/buscarContaDescricao/{desc}")
	public ResponseEntity<List<ContaPagar>> buscarContaPorDescricao(@PathVariable("desc") String desc) throws ExceptionLojaVirtual {
		
		List<ContaPagar> contasPagar = contaPagarRepository.buscarContaDescricao(desc.toUpperCase());
		
		if (contasPagar.isEmpty()) {
			throw new ExceptionLojaVirtual("Não foi possível encontrar a Conta a Pagar com a descrição: " + desc);
		}
		
		return new ResponseEntity<List<ContaPagar>>(contasPagar, HttpStatus.OK);
	}
	
}
