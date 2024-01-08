package cesar.montaldi.lojavirtual.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cesar.montaldi.lojavirtual.ExceptionLojaVirtual;
import cesar.montaldi.lojavirtual.model.AvaliacaoProduto;
import cesar.montaldi.lojavirtual.repository.AvaliacaoProdutoRepository;

@RestController
public class AvaliacaoProdutoController {

	@Autowired
	private AvaliacaoProdutoRepository avaliacaoProdutoRepository;
	
	@ResponseBody
	@PostMapping("/salvarAvaliacaoProduto")
	public ResponseEntity<AvaliacaoProduto>salvarAvaliacaoProduto(@RequestBody @Valid AvaliacaoProduto avaliacaoProduto) throws ExceptionLojaVirtual {
		
		if (avaliacaoProduto.getEmpresa() == null || (avaliacaoProduto.getEmpresa() != null && avaliacaoProduto.getEmpresa().getId() <= 0)) {
			throw new ExceptionLojaVirtual("Informe a empresa dona do registro.");
		}
		
		if (avaliacaoProduto.getProduto() == null || (avaliacaoProduto.getProduto() != null && avaliacaoProduto.getProduto().getId() <= 0)) {
			throw new ExceptionLojaVirtual("A avaliação deve conter o produto associado.");
		}
		
		if (avaliacaoProduto.getPessoa() == null || (avaliacaoProduto.getPessoa() != null && avaliacaoProduto.getPessoa().getId() <= 0)) {
			throw new ExceptionLojaVirtual("A avaliação deve conter a pessoa ou cliente associado.");
		}
		
		avaliacaoProduto = avaliacaoProdutoRepository.saveAndFlush(avaliacaoProduto);
		
		return new ResponseEntity<AvaliacaoProduto>(avaliacaoProduto, HttpStatus.OK);
	}
	
	
	@ResponseBody
	@GetMapping(value = "/buscaAvaliacaoProduto/{idProduto}")
	public ResponseEntity<List<AvaliacaoProduto>> avaliacaoProduto(@PathVariable("idProduto") Long idProduto) throws ExceptionLojaVirtual {
		
		List<AvaliacaoProduto> avaliacaoProdutos = avaliacaoProdutoRepository.avaliacaoProduto(idProduto);
		
		if (avaliacaoProdutos.isEmpty()) {
			throw new ExceptionLojaVirtual("Não encontramos nenhuma avaliação.");
		}
		
		return new ResponseEntity<List<AvaliacaoProduto>>(avaliacaoProdutos, HttpStatus.OK);
	}
	
	
	@ResponseBody
	@GetMapping(value = "/buscaAvaliacaoPessoa/{idPessoa}")
	public ResponseEntity<List<AvaliacaoProduto>> avaliacaoPessoa(@PathVariable("idPessoa") Long idPessoa) throws ExceptionLojaVirtual {
		
		List<AvaliacaoProduto> avaliacaoProdutos = avaliacaoProdutoRepository.avaliacaoPessoa(idPessoa);
		
		if (avaliacaoProdutos.isEmpty()) {
			throw new ExceptionLojaVirtual("Não encontramos nenhuma avaliação.");
		}
		
		return new ResponseEntity<List<AvaliacaoProduto>>(avaliacaoProdutos, HttpStatus.OK);
	}
	
	
	@ResponseBody
	@GetMapping(value = "/buscaAvaliacaoProdutoPessoa/{idProduto}/{idPessoa}")
	public ResponseEntity<List<AvaliacaoProduto>> 
		avaliacaoPessoa(@PathVariable("idProduto") Long idProduto, @PathVariable("idPessoa") Long idPessoa) throws ExceptionLojaVirtual {
		
		List<AvaliacaoProduto> avaliacaoProdutos = avaliacaoProdutoRepository.avaliacaoProdutoPessoa(idProduto, idPessoa);
		
		if (avaliacaoProdutos.isEmpty()) {
			throw new ExceptionLojaVirtual("Não encontramos nenhuma avaliação.");
		}
		
		return new ResponseEntity<List<AvaliacaoProduto>>(avaliacaoProdutos, HttpStatus.OK);
	}
	
	
	@ResponseBody
	@DeleteMapping(value = "/deleteAvaliacaoPessoa/{idAvaliacao}")
	public ResponseEntity<?> deleteAvaliacaoPessoa(@PathVariable("idAvaliacao") Long idAvaliacao) {
		
		if (!avaliacaoProdutoRepository.existsById(idAvaliacao)) {
			
			return new ResponseEntity<String>("Avaliação já foi removida.", HttpStatus.NOT_FOUND);
		}
		
		avaliacaoProdutoRepository.deleteById(idAvaliacao);
		
		return new ResponseEntity<String>("Avaliação do removida.", HttpStatus.OK);
	}
}
