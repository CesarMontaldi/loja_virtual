package cesar.montaldi.lojavirtual.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cesar.montaldi.lojavirtual.ExceptionLojaVirtual;
import cesar.montaldi.lojavirtual.model.Endereco;
import cesar.montaldi.lojavirtual.model.ItemVendaLoja;
import cesar.montaldi.lojavirtual.model.PessoaFisica;
import cesar.montaldi.lojavirtual.model.VendaCompraLojaVirtual;
import cesar.montaldi.lojavirtual.model.dto.ImagemProdutoDTO;
import cesar.montaldi.lojavirtual.model.dto.ItemVendaDTO;
import cesar.montaldi.lojavirtual.model.dto.ProdutoDTO;
import cesar.montaldi.lojavirtual.model.dto.VendaCompraLojaVirtualDTO;
import cesar.montaldi.lojavirtual.repository.EnderecoRepository;
import cesar.montaldi.lojavirtual.repository.NotaFiscalVendaRepository;
import cesar.montaldi.lojavirtual.repository.VendaCompraLojaVirtualRepository;

@RestController
public class VendaCompraLojaVirtualController {
	
	@Autowired
	private VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;

	
	@Autowired
	private PessoaController pessoaController;
	
	@Autowired
	NotaFiscalVendaRepository notaFiscalVendaRepository;
	
	
	@ResponseBody
	@PostMapping(value = "/salvarVendaLojatt")
	public ResponseEntity<VendaCompraLojaVirtualDTO> salvarVendaLoja(@RequestBody @Valid VendaCompraLojaVirtual vendaCompraLojaVirtual) throws ExceptionLojaVirtual {
		
		
		vendaCompraLojaVirtual.getPessoa().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		PessoaFisica pessoaFisica = pessoaController.salvarPf(vendaCompraLojaVirtual.getPessoa()).getBody();
		vendaCompraLojaVirtual.setPessoa(pessoaFisica);
		
		vendaCompraLojaVirtual.getEnderecoCobranca().setPessoa(pessoaFisica);
		vendaCompraLojaVirtual.getEnderecoCobranca().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		Endereco enderecoCobranca = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoCobranca());
		vendaCompraLojaVirtual.setEnderecoCobranca(enderecoCobranca);
		
		vendaCompraLojaVirtual.getEnderecoEntrega().setPessoa(pessoaFisica);
		vendaCompraLojaVirtual.getEnderecoEntrega().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		Endereco enderecoEntrega = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoEntrega());
		vendaCompraLojaVirtual.setEnderecoEntrega(enderecoEntrega);
		
		vendaCompraLojaVirtual.getNotaFiscalVenda().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		
		for (int i = 0; i < vendaCompraLojaVirtual.getItemVendaLojas().size(); i++) {
			vendaCompraLojaVirtual.getItemVendaLojas().get(i).setEmpresa(vendaCompraLojaVirtual.getEmpresa());
			vendaCompraLojaVirtual.getItemVendaLojas().get(i).setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
		}
		
		/*Salva primeiro a venda e todos os dados*/
		vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.saveAndFlush(vendaCompraLojaVirtual);
		
		/*Associa a venda gravada no banco com a nota fiscal*/
		vendaCompraLojaVirtual.getNotaFiscalVenda().setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
		
		
		/*Persiste novamente as notas para ficar amarrada na venda*/
		notaFiscalVendaRepository.saveAndFlush(vendaCompraLojaVirtual.getNotaFiscalVenda());

		VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();
		compraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtual.getValorTotal());
		compraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoa());
		
		compraLojaVirtualDTO.setEntrega(vendaCompraLojaVirtual.getEnderecoEntrega());
		compraLojaVirtualDTO.setCobranca(vendaCompraLojaVirtual.getEnderecoCobranca());
		
		compraLojaVirtualDTO.setValorDesconto(vendaCompraLojaVirtual.getValorDesconto());
		compraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtual.getValorFrete());
		
		compraLojaVirtualDTO.setFormaPagamento(vendaCompraLojaVirtual.getFormaPagamento());
		
		for (ItemVendaLoja item: vendaCompraLojaVirtual.getItemVendaLojas()) {
			
			ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
			itemVendaDTO.setQuantidade(item.getQuantidade());
			ProdutoDTO produtoDTO = new ProdutoDTO();
			itemVendaDTO.setProduto(produtoDTO);
			produtoDTO.setId(item.getProduto().getId());
			produtoDTO.setDescricao(item.getProduto().getDescricao());
			//produtoDTO.setCategoriaProduto(item.getProduto().getCategoriaProduto());
			//itemVendaDTO.setProduto(item.getProduto());
			
			compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
		}

		
		return new ResponseEntity<VendaCompraLojaVirtualDTO>(compraLojaVirtualDTO, HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "/consultaVendaIdtt/{idVenda}")
	public ResponseEntity<VendaCompraLojaVirtualDTO> consultaVendaId(@PathVariable("idVenda") Long idVenda) {
		
		VendaCompraLojaVirtual compraLojaVirtual = vendaCompraLojaVirtualRepository.findById(idVenda).orElse(new VendaCompraLojaVirtual());
		
		VendaCompraLojaVirtualDTO compraLojaVirtualDTO =  new VendaCompraLojaVirtualDTO();
		
		compraLojaVirtualDTO.setId(compraLojaVirtual.getId());
		
		compraLojaVirtualDTO.setValorTotal(compraLojaVirtual.getValorTotal());
		compraLojaVirtualDTO.setPessoa(compraLojaVirtual.getPessoa());
		
		compraLojaVirtualDTO.setEntrega(compraLojaVirtual.getEnderecoEntrega());
		compraLojaVirtualDTO.setCobranca(compraLojaVirtual.getEnderecoCobranca());
		
		compraLojaVirtualDTO.setValorDesconto(compraLojaVirtual.getValorDesconto());
		compraLojaVirtualDTO.setValorFrete(compraLojaVirtual.getValorFrete());
		
		compraLojaVirtualDTO.setFormaPagamento(compraLojaVirtual.getFormaPagamento());
		
		for (ItemVendaLoja item: compraLojaVirtual.getItemVendaLojas()) {
					
					ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
					itemVendaDTO.setQuantidade(item.getQuantidade());
					ProdutoDTO produtoDTO = new ProdutoDTO();
					itemVendaDTO.setId(item.getId());
					itemVendaDTO.setProduto(produtoDTO);
					produtoDTO.setId(item.getProduto().getId());
					produtoDTO.setDescricao(item.getProduto().getDescricao());
					//produtoDTO.setCategoriaProduto(item.getProduto().getCategoriaProduto());
					produtoDTO.setValorVenda(item.getProduto().getValorVenda());
					//produtoDTO.setMarcaProduto(item.getProduto().getMarcaProduto());
					produtoDTO.setQuantidadeEstoque(item.getProduto().getQuantidadeAlertaEstoque());
					//itemVendaDTO.setProduto(item.getProduto());
					
					compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
				}
		
		return new ResponseEntity<VendaCompraLojaVirtualDTO>(compraLojaVirtualDTO, HttpStatus.OK);
	}
	
}
