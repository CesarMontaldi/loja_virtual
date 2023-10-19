package cesar.montaldi.lojavirtual.controller;

import java.util.List;
import java.util.function.Supplier;

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
import cesar.montaldi.lojavirtual.model.Acesso;
import cesar.montaldi.lojavirtual.repository.AcessoRepository;
import cesar.montaldi.lojavirtual.service.AcessoService;

@Controller
@RestController 
public class AcessoController {
	
	@Autowired
	private AcessoService acessoService;
	
	@Autowired
	private AcessoRepository acessoRepository;

	@ResponseBody /*Poder dar um retorno da API*/
	@PostMapping(value = "/salvarAcesso")/*Mapeando a url para receber JSON*/
	public ResponseEntity<Acesso> salvarAcesso(@RequestBody Acesso acesso) throws ExceptionLojaVirtual {/*Recebe o JSON e converte para Objeto*/
		
		if (acesso.getId() == null) {
			
			List<Acesso> acessos = acessoRepository.buscarAcessoDescricao(acesso.getDescricao().toUpperCase());
			
			if (!acessos.isEmpty()) {
				throw new ExceptionLojaVirtual("Já existe um Acesso com a descrição: " + acesso.getDescricao());
			}
		}
		
		Acesso acessoSalvo = acessoService.save(acesso);
		return new ResponseEntity<Acesso>(acessoSalvo, HttpStatus.OK);
	}
	
	@ResponseBody /*Poder dar um retorno da API*/
	@PostMapping(value = "/deleteAcesso")/*Mapeando a url para receber JSON*/
	public ResponseEntity<?> deleteAcesso(@RequestBody Acesso acesso) {/*Recebe o JSON e converte para Objeto*/
		
		acessoRepository.deleteById(acesso.getId());
		return new ResponseEntity("Acesso Removido", HttpStatus.OK);
	}
	
	@ResponseBody /*Poder dar um retorno da API*/
	@DeleteMapping(value = "/deleteAcessoId/{id}")/*Mapeando a url para receber JSON*/
	public ResponseEntity<?> deleteAcessoId(@PathVariable("id") Long id) {/*Recebe o JSON e converte para Objeto*/
		
		acessoRepository.deleteById(id);
		
		return new ResponseEntity("Acesso Removido", HttpStatus.OK);
	}
	
	
	
	@ResponseBody /*Poder dar um retorno da API*/
	@GetMapping(value = "/obterAcesso/{id}")/*Mapeando a url para receber JSON*/
	public ResponseEntity<?> obterAcesso(@PathVariable("id") Long id) throws ExceptionLojaVirtual {/*Recebe o JSON e converte para Objeto*/
		
		Acesso acesso = acessoRepository.findById(id).orElse(null);

		if (acesso == null) {
			throw new ExceptionLojaVirtual("Não foi possível encontrar o Acesso com o código: " + id);
		}
		
		return new ResponseEntity<Acesso>(acesso, HttpStatus.OK);
	}
	
	
	
	@ResponseBody /*Poder dar um retorno da API*/
	@GetMapping(value = "/buscarPorDesc/{desc}")/*Mapeando a url para receber JSON*/
	public ResponseEntity<List<Acesso>> buscarPorDesc(@PathVariable("desc") String desc) {/*Recebe o JSON e converte para Objeto*/
		
		List<Acesso> acessos = acessoRepository.buscarAcessoDescricao(desc.toUpperCase()); 
		
		return new ResponseEntity<List<Acesso>>(acessos, HttpStatus.OK);
	}
	
}
