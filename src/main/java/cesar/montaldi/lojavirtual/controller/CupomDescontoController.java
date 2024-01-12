package cesar.montaldi.lojavirtual.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cesar.montaldi.lojavirtual.model.CupomDesconto;
import cesar.montaldi.lojavirtual.repository.CupomDescontoRepository;

@RestController
public class CupomDescontoController {

	@Autowired
	private CupomDescontoRepository cupomDescontoRepository;
	

	@ResponseBody
	@GetMapping(value = "/listaCupomDesconto/{idEmpresa}")
	public ResponseEntity<List<CupomDesconto>> listaCupomDescontoIdEmpresa(@PathVariable("idEmpresa") Long idEmpresa) {
		
		List<CupomDesconto> cupomDesconto = cupomDescontoRepository.cupomDescontoPorEmpresa(idEmpresa);
		
		return new ResponseEntity<List<CupomDesconto>>(cupomDesconto, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/listaCupomDesconto")
	public ResponseEntity<List<CupomDesconto>> listaCupomsDesconto() {
		
		List<CupomDesconto> cupomsDesconto = cupomDescontoRepository.findAll();
		
		return new ResponseEntity<List<CupomDesconto>>(cupomsDesconto, HttpStatus.OK);
	}
}
