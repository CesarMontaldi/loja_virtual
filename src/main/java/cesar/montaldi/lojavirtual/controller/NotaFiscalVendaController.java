package cesar.montaldi.lojavirtual.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cesar.montaldi.lojavirtual.ExceptionLojaVirtual;
import cesar.montaldi.lojavirtual.model.NotaFiscalVenda;
import cesar.montaldi.lojavirtual.model.dto.NotaFiscalVendaDTO;
import cesar.montaldi.lojavirtual.repository.NotaFiscalVendaRepository;

@RestController
public class NotaFiscalVendaController {
	
	@Autowired
	private NotaFiscalVendaRepository notaFiscalVendaRepository;
	
	
	@ResponseBody 
	@GetMapping(value = "/buscarNotaFiscalCompraDaVenda/{idVenda}")
	public ResponseEntity<NotaFiscalVendaDTO> buscarNotaFiscalCompraDaVenda(@PathVariable("idVenda") Long idVenda) throws ExceptionLojaVirtual {
		
		NotaFiscalVenda notaFiscalVenda = notaFiscalVendaRepository.buscaNotaFiscalPorVendaUnica(idVenda);
		
		NotaFiscalVendaDTO notaFiscalVendaDTO = new NotaFiscalVendaDTO();
		
		if (notaFiscalVenda == null) {
			throw new ExceptionLojaVirtual("Não foi possível encontrar a Nota Fiscal de Venda com o código: " + idVenda);
		}
		
		notaFiscalVendaDTO.setId(notaFiscalVenda.getId());
		notaFiscalVendaDTO.setNumero(notaFiscalVenda.getNumero());
		notaFiscalVendaDTO.setSerie(notaFiscalVenda.getSerie());
		notaFiscalVendaDTO.setTipo(notaFiscalVenda.getTipo());
		notaFiscalVendaDTO.setEmpresa(notaFiscalVenda.getEmpresa().getId());
		notaFiscalVendaDTO.setPdf(notaFiscalVenda.getPdf());
		notaFiscalVendaDTO.setXml(notaFiscalVenda.getXml());
		notaFiscalVendaDTO.setVendaCompraLojaVirtual(notaFiscalVenda.getVendaCompraLojaVirtual().getId());
		notaFiscalVendaDTO.setPessoa(notaFiscalVenda.getVendaCompraLojaVirtual().getPessoa());
		notaFiscalVendaDTO.setValorTotal(notaFiscalVenda.getVendaCompraLojaVirtual().getValorTotal());
		notaFiscalVendaDTO.setFormaPagamento(notaFiscalVenda.getVendaCompraLojaVirtual().getFormaPagamento().getDescricao());
		
		
		return new ResponseEntity<NotaFiscalVendaDTO>(notaFiscalVendaDTO, HttpStatus.OK);
	}
	

}
