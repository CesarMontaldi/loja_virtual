package cesar.montaldi.lojavirtual.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cesar.montaldi.lojavirtual.ExceptionLojaVirtual;
import cesar.montaldi.lojavirtual.model.NotaItemProduto;
import cesar.montaldi.lojavirtual.repository.NotaItemProdutoRepository;

@RestController
public class NotaItemProdutoController {
	
	@Autowired
	private NotaItemProdutoRepository notaItemProdutoRepository;
	
	@ResponseBody
	@PostMapping("/salvarNotaItemProduto")
	public ResponseEntity<NotaItemProduto> salvarNotaItemProduto(@RequestBody @Valid NotaItemProduto notaItemProduto) throws ExceptionLojaVirtual {
		
		if (notaItemProduto.getId() == null) {
			
			if (notaItemProduto.getProduto() == null || notaItemProduto.getProduto().getId() <= 0) {
				throw new ExceptionLojaVirtual("O produto deve ser informado.");
			}
			
			if (notaItemProduto.getNotaFiscalCompra() == null || notaItemProduto.getNotaFiscalCompra().getId() <= 0) {
				throw new ExceptionLojaVirtual("A nota fiscal deve ser informada.");
			}
			
			if (notaItemProduto.getEmpresa() == null || notaItemProduto.getEmpresa().getId() <= 0) {
				throw new ExceptionLojaVirtual("A empresa deve ser informada.");
			}
			
			List<NotaItemProduto> notaExiste = notaItemProdutoRepository.
					buscaNotaItemPorProdutoNota(notaItemProduto.getProduto().getId(),
							notaItemProduto.getNotaFiscalCompra().getId());
			
			if (!notaExiste.isEmpty()) {
				throw new ExceptionLojaVirtual("Já existe este produto cadastrado para essa nota.");
			}
			
			if (notaItemProduto.getQuantidade() <= 0) {
				throw new ExceptionLojaVirtual("A quantidade do produto deve ser informada.");
			}
		}
		
		NotaItemProduto notaItemSalva = notaItemProdutoRepository.save(notaItemProduto);
		
		return new ResponseEntity<NotaItemProduto>(notaItemSalva, HttpStatus.OK); 
	}
	
	@ResponseBody 
	@DeleteMapping(value = "/deleteNotaItemId/{id}")
	public ResponseEntity<String> deleteNotaItemId(@PathVariable("id") Long id) {
		
		if (!notaItemProdutoRepository.findById(id).isPresent()) {
			return new ResponseEntity<String>("Nota Item Produto já foi removida.", HttpStatus.NOT_FOUND);
		}
		
		notaItemProdutoRepository.deleteById(id);
		
		return new ResponseEntity<String>("Nota Item Produto Removida", HttpStatus.OK);
	}

}
