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
import cesar.montaldi.lojavirtual.model.Produto;
import cesar.montaldi.lojavirtual.repository.ProdutoRepository;

@Controller
@RestController 
public class ProdutoController {
	
	
	@Autowired
	private ProdutoRepository produtoRepository;

	@ResponseBody /*Poder dar um retorno da API*/
	@PostMapping(value = "/salvarProduto")/*Mapeando a url para receber JSON*/
	public ResponseEntity<Produto> salvarProduto(@RequestBody @Valid Produto produto) throws ExceptionLojaVirtual {/*Recebe o JSON e converte para Objeto*/
		

		if (produto.getEmpresa() == null || produto.getEmpresa().getId() <= 0) {
			throw new ExceptionLojaVirtual("Empresa responsável deve ser informada.");
		}
		
		if (produto.getId() == null) {
			
			List<Produto> produtos = produtoRepository.buscarProdutoPorNome(produto.getNome().toUpperCase(), produto.getEmpresa().getId());
			
			if (!produtos.isEmpty()) {
				throw new ExceptionLojaVirtual("Já existe um Produto com esse nome: " + produto.getNome());
			}
		}
		
		if (produto.getCategoriaProduto() == null || produto.getCategoriaProduto().getId() <= 0) {
			throw new ExceptionLojaVirtual("Categoria do produto deve ser informada.");
		}
		
		if (produto.getMarcaProduto() == null || produto.getMarcaProduto().getId() <= 0) {
			throw new ExceptionLojaVirtual("Marca do produto deve ser informada.");
		}
		
		Produto produtoSalvo = produtoRepository.save(produto);
		
		return new ResponseEntity<Produto>(produtoSalvo, HttpStatus.OK);
	}
	
	@ResponseBody /*Poder dar um retorno da API*/
	@PostMapping(value = "/deleteProduto")/*Mapeando a url para receber JSON*/
	public ResponseEntity<?> deleteProduto(@RequestBody Produto produto) {/*Recebe o JSON e converte para Objeto*/
		
		produtoRepository.deleteById(produto.getId());
		return new ResponseEntity("Produto Removido", HttpStatus.OK);
	}
	
	@ResponseBody /*Poder dar um retorno da API*/
	@DeleteMapping(value = "/deleteProdutoId/{id}")/*Mapeando a url para receber JSON*/
	public ResponseEntity<?> deleteProdutoId(@PathVariable("id") Long id) {/*Recebe o JSON e converte para Objeto*/
		
		produtoRepository.deleteById(id);
		
		return new ResponseEntity("Produto Removido", HttpStatus.OK);
	}
	
	
	
	@ResponseBody /*Poder dar um retorno da API*/
	@GetMapping(value = "/buscaProdutoId/{id}")/*Mapeando a url para receber JSON*/
	public ResponseEntity<?> buscaProdutoId(@PathVariable("id") Long id) throws ExceptionLojaVirtual {/*Recebe o JSON e converte para Objeto*/
		
		Produto produto = produtoRepository.findById(id).orElse(null);

		if (produto == null) {
			throw new ExceptionLojaVirtual("Não foi possível encontrar o Produto com o código: " + id);
		}
		
		return new ResponseEntity<Produto>(produto, HttpStatus.OK);
	}
	
	
	
	@ResponseBody /*Poder dar um retorno da API*/
	@GetMapping(value = "/buscaProdutoNome/{nome}")/*Mapeando a url para receber JSON*/
	public ResponseEntity<List<Produto>> buscaProdutoPorNome(@PathVariable("nome") String nome) {/*Recebe o JSON e converte para Objeto*/
		
		List<Produto> produtos = produtoRepository.buscarProdutoPorNome(nome.toUpperCase()); 
		
		return new ResponseEntity<List<Produto>>(produtos, HttpStatus.OK);
	}
	
}
