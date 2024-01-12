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
import cesar.montaldi.lojavirtual.model.NotaFiscalCompra;
import cesar.montaldi.lojavirtual.model.NotaFiscalVenda;
import cesar.montaldi.lojavirtual.repository.NotaFiscalCompraRepository;
import cesar.montaldi.lojavirtual.repository.NotaFiscalVendaRepository;

@RestController
public class NotaFiscalCompraController {
	
	@Autowired
	private NotaFiscalCompraRepository notaFiscalCompraRepository;
	

	@ResponseBody 
	@PostMapping(value = "/salvarNotaFiscalCompra")
	public ResponseEntity<NotaFiscalCompra> salvarNotaFiscalCompra(@RequestBody @Valid NotaFiscalCompra notaFiscalCompra) throws ExceptionLojaVirtual {
		
		if (notaFiscalCompra.getId() == null) {
			
			if (notaFiscalCompra.getDescricaoObs() != null) {
				List<NotaFiscalCompra> notaFiscalCompras = notaFiscalCompraRepository.buscarNotaDescricao(notaFiscalCompra.getDescricaoObs().toUpperCase().trim()); 
				
				if (!notaFiscalCompras.isEmpty()) {
					throw new ExceptionLojaVirtual("Já existe uma Nota Fiscal Compra com a descrição: " + notaFiscalCompra.getDescricaoObs());
				}
			}
					
		}
		
		if (notaFiscalCompra.getPessoa() == null || notaFiscalCompra.getPessoa().getId() <= 0) {
			throw new ExceptionLojaVirtual("A Pessoa Juridica da nota fiscal deve ser informada.");
		}
		
		if (notaFiscalCompra.getEmpresa() == null || notaFiscalCompra.getEmpresa().getId() <= 0) {
			throw new ExceptionLojaVirtual("A Empresa responsável deve ser informada.");
		}
		
		if (notaFiscalCompra.getContaPagar() == null || notaFiscalCompra.getContaPagar().getId() <= 0) {
			throw new ExceptionLojaVirtual("A conta a pagar da nota deve ser informada.");
		}
		
		
		NotaFiscalCompra notaFiscalCompraSalva = notaFiscalCompraRepository.save(notaFiscalCompra);
		
		return new ResponseEntity<NotaFiscalCompra>(notaFiscalCompraSalva, HttpStatus.OK);
	}
	
	
	@ResponseBody 
	@DeleteMapping(value = "/deleteNotaFiscalCompraId/{id}")
	public ResponseEntity<?> deleteNotaFiscalCompraId(@PathVariable("id") Long id) {
		
		if (!notaFiscalCompraRepository.findById(id).isPresent()) {
			return new ResponseEntity<String>("Nota Fiscal de Compra já foi removida.", HttpStatus.NOT_FOUND);
		}
		
		notaFiscalCompraRepository.deleteItemNotaFiscalCompra(id);/*Deleta os filhos*/
		notaFiscalCompraRepository.deleteById(id);/*Deleta o pai*/
		
		return new ResponseEntity<String>("Nota Fiscal de Compra Removida", HttpStatus.OK);
	}
	
	
	@ResponseBody 
	@GetMapping(value = "/buscarNotaFiscalCompra/{id}")
	public ResponseEntity<NotaFiscalCompra> buscarNotaFiscalCompra(@PathVariable("id") Long id) throws ExceptionLojaVirtual {
		
		NotaFiscalCompra notaFiscalCompra = notaFiscalCompraRepository.findById(id).orElse(null);

		if (notaFiscalCompra == null) {
			throw new ExceptionLojaVirtual("Não foi possível encontrar a Nota Fiscal de Compra com o código: " + id);
		}
		
		return new ResponseEntity<NotaFiscalCompra>(notaFiscalCompra, HttpStatus.OK);
	}
	

	@ResponseBody 
	@GetMapping(value = "/buscarNotaFiscalCompraDescricao/{desc}")
	public ResponseEntity<List<NotaFiscalCompra>> buscarNotaPorDescricao(@PathVariable("desc") String desc) throws ExceptionLojaVirtual {
		
		List<NotaFiscalCompra> notaFiscalCompras = notaFiscalCompraRepository.buscarNotaDescricao(desc.toUpperCase());
		
		if (notaFiscalCompras.isEmpty()) {
			throw new ExceptionLojaVirtual("Não foi possível encontrar a Nota Fiscal de Compra com a descrição: " + desc);	
		} 
		
		return new ResponseEntity<List<NotaFiscalCompra>>(notaFiscalCompras, HttpStatus.OK);
	}

}
