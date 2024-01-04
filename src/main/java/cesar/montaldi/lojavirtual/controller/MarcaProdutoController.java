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
import cesar.montaldi.lojavirtual.model.Acesso;
import cesar.montaldi.lojavirtual.model.MarcaProduto;
import cesar.montaldi.lojavirtual.repository.MarcaProdutoRepository;

@Controller
@RestController 
public class MarcaProdutoController {

	
	@Autowired
	private MarcaProdutoRepository marcaProdutoRepository;

	@ResponseBody 
	@PostMapping(value = "/salvarMarca")
	public ResponseEntity<MarcaProduto> salvarMarca(@RequestBody @Valid MarcaProduto marcaProduto) throws ExceptionLojaVirtual {
		
		if (marcaProduto.getId() == null) {
			List<MarcaProduto> marcaProdutos = marcaProdutoRepository.buscarMarcaDescricao(marcaProduto.getNomeDescricao().toUpperCase());
			
			if (!marcaProdutos.isEmpty()) {
				throw new ExceptionLojaVirtual("Já existe uma Marca com a descrição: " + marcaProduto.getNomeDescricao());
			}
		}
		
		MarcaProduto marcaProdutoSalvo = marcaProdutoRepository.save(marcaProduto);
		return new ResponseEntity<MarcaProduto>(marcaProdutoSalvo, HttpStatus.OK);
	}
	
	@ResponseBody 
	@PostMapping(value = "/deleteMarca")
	public ResponseEntity<String> deleteMarca(@RequestBody MarcaProduto marcaProduto) {
		

		if (!marcaProdutoRepository.findById(marcaProduto.getId()).isPresent()) {
			return new ResponseEntity<String>("Marca Produto já foi removida.", HttpStatus.NOT_FOUND);
		}
		
		marcaProdutoRepository.deleteById(marcaProduto.getId());
		
		return new ResponseEntity<String>("Marca Produto Removida", HttpStatus.OK);
	}
	
	@ResponseBody 
	@DeleteMapping(value = "/deleteMarcaId/{id}")
	public ResponseEntity<String> deleteMarcaId(@PathVariable("id") Long id) {
		
		if (!marcaProdutoRepository.findById(id).isPresent()) {
			return new ResponseEntity<String>("Marca Produto já foi removida.", HttpStatus.NOT_FOUND);
		}
		
		marcaProdutoRepository.deleteById(id);
		
		return new ResponseEntity<String>("Marca Produto Removida", HttpStatus.OK);
	}
	 
	
	@ResponseBody 
	@GetMapping(value = "/buscarMarca/{id}")
	public ResponseEntity<MarcaProduto> buscarMarca(@PathVariable("id") Long id) throws ExceptionLojaVirtual {
		
		MarcaProduto marcaProduto = marcaProdutoRepository.findById(id).orElse(null);

		if (marcaProduto == null) {
			throw new ExceptionLojaVirtual("Não foi possível encontrar a Marca com o código: " + id);
		}
		
		return new ResponseEntity<MarcaProduto>(marcaProduto, HttpStatus.OK);
	}
	
	
	@ResponseBody 
	@GetMapping(value = "/buscarMarcaProdutoDescricao/{desc}")
	public ResponseEntity<List<MarcaProduto>> buscarMarcaPorDescricao(@PathVariable("desc") String desc) throws ExceptionLojaVirtual {
		
		List<MarcaProduto> marcaProdutos = marcaProdutoRepository.buscarMarcaDescricao(desc.toUpperCase());
		
		if (marcaProdutos.isEmpty()) {
			throw new ExceptionLojaVirtual("Não foi possível encontrar a Marca com a descrição: " + desc);	
		} 
		
		return new ResponseEntity<List<MarcaProduto>>(marcaProdutos, HttpStatus.OK);
	}
	
}
