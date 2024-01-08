package cesar.montaldi.lojavirtual.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cesar.montaldi.lojavirtual.ExceptionLojaVirtual;
import cesar.montaldi.lojavirtual.model.FormaPagamento;
import cesar.montaldi.lojavirtual.repository.FormaPagamentoRepository;

@RestController
public class FormaPagamentoController {
	
	@Autowired
	private FormaPagamentoRepository formaPagamentoRepository;
	
	@ResponseBody /*Poder dar um retorno da API*/
	@PostMapping(value = "/salvarFormaPagamento")/*Mapeando a url para receber JSON*/
	public ResponseEntity<FormaPagamento> salvarAcesso(@RequestBody @Valid FormaPagamento formaPagamento) throws ExceptionLojaVirtual {/*Recebe o JSON e converte para Objeto*/

		
		formaPagamento = formaPagamentoRepository.save(formaPagamento);
		return new ResponseEntity<FormaPagamento>(formaPagamento, HttpStatus.OK);
	}
}
