package cesar.montaldi.lojavirtual.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cesar.montaldi.lojavirtual.ExceptionLojaVirtual;
import cesar.montaldi.lojavirtual.model.Endereco;
import cesar.montaldi.lojavirtual.model.ImagemProduto;
import cesar.montaldi.lojavirtual.model.ItemVendaLoja;
import cesar.montaldi.lojavirtual.model.PessoaFisica;
import cesar.montaldi.lojavirtual.model.StatusRastreio;
import cesar.montaldi.lojavirtual.model.VendaCompraLojaVirtual;
import cesar.montaldi.lojavirtual.model.dto.ImagemProdutoDTO;
import cesar.montaldi.lojavirtual.model.dto.ItemVendaDTO;
import cesar.montaldi.lojavirtual.model.dto.ProdutoDTO;
import cesar.montaldi.lojavirtual.model.dto.VendaCompraLojaVirtualDTO;
import cesar.montaldi.lojavirtual.repository.EnderecoRepository;
import cesar.montaldi.lojavirtual.repository.ImagemProdutoRepository;
import cesar.montaldi.lojavirtual.repository.NotaFiscalVendaRepository;
import cesar.montaldi.lojavirtual.repository.StatusRastreioRepository;
import cesar.montaldi.lojavirtual.repository.VendaCompraLojaVirtualRepository;
import cesar.montaldi.lojavirtual.service.VendaService;

@RestController
public class VendaCompraLojaVirtualController {
	
	@Autowired
	private VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private PessoaController pessoaController;
	
	@Autowired
	private NotaFiscalVendaRepository notaFiscalVendaRepository;
	
	@Autowired
	private ImagemProdutoRepository imagemProdutoRepository;
	
	@Autowired
	private StatusRastreioRepository statusRastreioRepository;
	
	@Autowired
	private VendaService vendaService;
	
	
	@ResponseBody
	@PostMapping(value = "/salvarVendaLoja")
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
		
		StatusRastreio statusRastreio = new StatusRastreio();
		statusRastreio.setCentroDistribuicao("Loja local");
		statusRastreio.setCidade("Sumaré");
		statusRastreio.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		statusRastreio.setEstado("São Paulo");
		statusRastreio.setStatus("Inicio Compra");
		statusRastreio.setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
		
		statusRastreioRepository.save(statusRastreio);
		
		/*Associa a venda gravada no banco com a nota fiscal*/
		vendaCompraLojaVirtual.getNotaFiscalVenda().setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
		
		
		/*Persiste novamente as notas para ficar amarrada na venda*/
		notaFiscalVendaRepository.saveAndFlush(vendaCompraLojaVirtual.getNotaFiscalVenda());

		VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();
		compraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtual.getValorTotal());
		compraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoa());
		
		compraLojaVirtualDTO.setEntrega(vendaCompraLojaVirtual.getEnderecoEntrega());
		compraLojaVirtualDTO.setCobranca(vendaCompraLojaVirtual.getEnderecoCobranca());
		compraLojaVirtualDTO.setId(vendaCompraLojaVirtual.getId());
		
		compraLojaVirtualDTO.setValorDesconto(vendaCompraLojaVirtual.getValorDesconto());
		compraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtual.getValorFrete());				
		compraLojaVirtualDTO.setFormaPagamento(vendaCompraLojaVirtual.getFormaPagamento());
		
		for (ItemVendaLoja item: vendaCompraLojaVirtual.getItemVendaLojas()) {
			
			ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
			itemVendaDTO.setQuantidade(item.getQuantidade());
			ProdutoDTO produtoDTO = new ProdutoDTO();
			itemVendaDTO.setProduto(produtoDTO);
			produtoDTO.setId(item.getProduto().getId());
			produtoDTO.setNome(item.getProduto().getNome());
			produtoDTO.setDescricao(item.getProduto().getDescricao());

			
			compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
		}

		
		return new ResponseEntity<VendaCompraLojaVirtualDTO>(compraLojaVirtualDTO, HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "/consultaVendaId/{idVenda}")
	public ResponseEntity<VendaCompraLojaVirtualDTO> consultaVendaId(@PathVariable("idVenda") Long idVenda) {
		
		VendaCompraLojaVirtual compraLojaVirtual = vendaCompraLojaVirtualRepository.findByIdExclusao(idVenda);
		
		if (compraLojaVirtual == null) {
			compraLojaVirtual = new VendaCompraLojaVirtual();
		}
		
		VendaCompraLojaVirtualDTO compraLojaVirtualDTO =  new VendaCompraLojaVirtualDTO();
		
		compraLojaVirtualDTO.setId(compraLojaVirtual.getId());
		
		compraLojaVirtualDTO.setValorTotal(compraLojaVirtual.getValorTotal());
		compraLojaVirtualDTO.setPessoa(compraLojaVirtual.getPessoa());
		
		compraLojaVirtualDTO.setEntrega(compraLojaVirtual.getEnderecoEntrega());
		compraLojaVirtualDTO.setCobranca(compraLojaVirtual.getEnderecoCobranca());
		
		compraLojaVirtualDTO.setValorDesconto(compraLojaVirtual.getValorDesconto());
		compraLojaVirtualDTO.setValorFrete(compraLojaVirtual.getValorFrete());
		
		compraLojaVirtualDTO.setFormaPagamento(compraLojaVirtual.getFormaPagamento());
		
		Long id = null;
		
		for (ItemVendaLoja item: compraLojaVirtual.getItemVendaLojas()) {

			id = item.getProduto().getId();
				
			List<ImagemProdutoDTO> dtosimages = new ArrayList<ImagemProdutoDTO>();
			List<ImagemProduto> imagemProdutos = imagemProdutoRepository.buscaImagemProduto(id);
			
			for (ImagemProduto imagemProduto : imagemProdutos) {
				
				ImagemProdutoDTO imagemProdutoDTO = new ImagemProdutoDTO();
				imagemProdutoDTO.setId(imagemProduto.getId());
				imagemProdutoDTO.setEmpresa(imagemProduto.getEmpresa().getId());
				imagemProdutoDTO.setProduto(imagemProduto.getProduto().getId());
				imagemProdutoDTO.setImagemOriginal(imagemProduto.getImagemOriginal());
				imagemProdutoDTO.setImagemMiniatura(imagemProduto.getImagemMiniatura());
				
				dtosimages.add(imagemProdutoDTO);
			
			}
		
				ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
				itemVendaDTO.setQuantidade(item.getQuantidade());
				ProdutoDTO produtoDTO = new ProdutoDTO();
				itemVendaDTO.setId(item.getId());
				itemVendaDTO.setProduto(produtoDTO);
				produtoDTO.setId(item.getProduto().getId());
				produtoDTO.setNome(item.getProduto().getNome());
				produtoDTO.setDescricao(item.getProduto().getDescricao());
				produtoDTO.setPeso(item.getProduto().getPeso());
				produtoDTO.setLargura(item.getProduto().getLargura());
				produtoDTO.setAltura(item.getProduto().getAltura());
				produtoDTO.setProfundidade(item.getProduto().getProfundidade());
				produtoDTO.setValorVenda(item.getProduto().getValorVenda());
				produtoDTO.setQuantidadeEstoque(item.getProduto().getQuantidadeEstoque());
				produtoDTO.setEmpresa(item.getProduto().getEmpresa());
				produtoDTO.setCategoriaProduto(item.getProduto().getCategoriaProduto());
				produtoDTO.setMarcaProduto(item.getProduto().getMarcaProduto());
				produtoDTO.setImagens(dtosimages);
				
				compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
				
		}
		
		return new ResponseEntity<VendaCompraLojaVirtualDTO>(compraLojaVirtualDTO, HttpStatus.OK);
	}
	
	
	@ResponseBody
	@GetMapping(value = "/consultaVendaDinamica/{valor}/{tipoconsulta}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>> consultaVendaDinamica(@PathVariable("valor") String valor, @PathVariable("tipoconsulta") String tipoconsulta) {
		
		List<VendaCompraLojaVirtual> compraLojaVirtual = null;
		
		if (tipoconsulta.equalsIgnoreCase("POR_ID_PROD")) {
			compraLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorProduto(Long.parseLong(valor));
		
		} else if (tipoconsulta.equalsIgnoreCase("POR_NOME_PROD")) {
			compraLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorNomeProduto(valor.toUpperCase().trim());
		
		} else if (tipoconsulta.equalsIgnoreCase("POR_NOME_CLIENTE")) {
			compraLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorNomeCliente(valor.toUpperCase().trim());
		
		} else if (tipoconsulta.equalsIgnoreCase("POR_ENDERECO_COBRANCA")) {
			compraLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorEnderecoCobranca(valor.toUpperCase().trim());
		
		} else if (tipoconsulta.equalsIgnoreCase("POR_ENDERECO_ENTREGA")) {
			compraLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorEnderecoEntrega(valor.toUpperCase().trim());
		}
		
		if (compraLojaVirtual == null) {
			compraLojaVirtual = new ArrayList<VendaCompraLojaVirtual>();
		}
		
		List<VendaCompraLojaVirtualDTO> dtosVendas = new ArrayList<VendaCompraLojaVirtualDTO>();
		
		for (VendaCompraLojaVirtual vendaCompraLojaVirtual : compraLojaVirtual) {
				
			VendaCompraLojaVirtualDTO compraLojaVirtualDTO =  new VendaCompraLojaVirtualDTO();
			
			compraLojaVirtualDTO.setId(vendaCompraLojaVirtual.getId());
			
			compraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtual.getValorTotal());
			compraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoa());
			
			compraLojaVirtualDTO.setEntrega(vendaCompraLojaVirtual.getEnderecoEntrega());
			compraLojaVirtualDTO.setCobranca(vendaCompraLojaVirtual.getEnderecoCobranca());
			
			compraLojaVirtualDTO.setValorDesconto(vendaCompraLojaVirtual.getValorDesconto());
			compraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtual.getValorFrete());
			
			compraLojaVirtualDTO.setFormaPagamento(vendaCompraLojaVirtual.getFormaPagamento());
			
			Long id = null;
			
			for (ItemVendaLoja item: vendaCompraLojaVirtual.getItemVendaLojas()) {
	
				id = item.getProduto().getId();
					
				List<ImagemProdutoDTO> dtosimages = new ArrayList<ImagemProdutoDTO>();
				List<ImagemProduto> imagemProdutos = imagemProdutoRepository.buscaImagemProduto(id);
				
				for (ImagemProduto imagemProduto : imagemProdutos) {
					
					ImagemProdutoDTO imagemProdutoDTO = new ImagemProdutoDTO();
					imagemProdutoDTO.setId(imagemProduto.getId());
					imagemProdutoDTO.setEmpresa(imagemProduto.getEmpresa().getId());
					imagemProdutoDTO.setProduto(imagemProduto.getProduto().getId());
					//imagemProdutoDTO.setImagemOriginal(imagemProduto.getImagemOriginal());
					//imagemProdutoDTO.setImagemMiniatura(imagemProduto.getImagemMiniatura());
					
					dtosimages.add(imagemProdutoDTO);
				
				}
			
					ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
					itemVendaDTO.setQuantidade(item.getQuantidade());
					ProdutoDTO produtoDTO = new ProdutoDTO();
					itemVendaDTO.setId(item.getId());
					itemVendaDTO.setProduto(produtoDTO);
					produtoDTO.setId(item.getProduto().getId());
					produtoDTO.setNome(item.getProduto().getNome());
					produtoDTO.setDescricao(item.getProduto().getDescricao());
					produtoDTO.setPeso(item.getProduto().getPeso());
					produtoDTO.setLargura(item.getProduto().getLargura());
					produtoDTO.setAltura(item.getProduto().getAltura());
					produtoDTO.setProfundidade(item.getProduto().getProfundidade());
					produtoDTO.setValorVenda(item.getProduto().getValorVenda());
					produtoDTO.setQuantidadeEstoque(item.getProduto().getQuantidadeEstoque());
					produtoDTO.setEmpresa(item.getProduto().getEmpresa());
					produtoDTO.setCategoriaProduto(item.getProduto().getCategoriaProduto());
					produtoDTO.setMarcaProduto(item.getProduto().getMarcaProduto());
					produtoDTO.setImagens(dtosimages);
					
					compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
			}
			
			dtosVendas.add(compraLojaVirtualDTO);
		}
		
		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(dtosVendas, HttpStatus.OK);
	}
	
	
	
	@ResponseBody
	@GetMapping(value = "/consultaVendaDinamicaFaixaData/{data1}/{data2}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>>consultaVendaDinamicaFaixaData(@PathVariable("data1") String data1, @PathVariable("data2") String data2) throws ParseException {
		
		List<VendaCompraLojaVirtual> compraLojaVirtual = null;
		
		//SimpleDateFormat dateFormatOrig = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		//Date date1 = dateFormatOrig.parse(data1);
		//Date date2 = dateFormatOrig.parse(data2);
		
		Date date1 = dateFormat.parse(data1);
		Date date2 = dateFormat.parse(data2);
	
		
		compraLojaVirtual = vendaCompraLojaVirtualRepository.consultaVendaFaixaData(date1, date2);
		
		if (compraLojaVirtual == null) {
			compraLojaVirtual = new ArrayList<VendaCompraLojaVirtual>();
		}
		
		List<VendaCompraLojaVirtualDTO> dtosVendas = new ArrayList<VendaCompraLojaVirtualDTO>();
		
		for (VendaCompraLojaVirtual vendaCompraLojaVirtual : compraLojaVirtual) {
			
			VendaCompraLojaVirtualDTO compraLojaVirtualDTO =  new VendaCompraLojaVirtualDTO();
			
			compraLojaVirtualDTO.setId(vendaCompraLojaVirtual.getId());
			
			compraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtual.getValorTotal());
			compraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoa());
			
			compraLojaVirtualDTO.setEntrega(vendaCompraLojaVirtual.getEnderecoEntrega());
			compraLojaVirtualDTO.setCobranca(vendaCompraLojaVirtual.getEnderecoCobranca());
			
			compraLojaVirtualDTO.setValorDesconto(vendaCompraLojaVirtual.getValorDesconto());
			compraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtual.getValorFrete());
			
			compraLojaVirtualDTO.setFormaPagamento(vendaCompraLojaVirtual.getFormaPagamento());
			
			Long id = null;
			
			for (ItemVendaLoja item: vendaCompraLojaVirtual.getItemVendaLojas()) {
	
				id = item.getProduto().getId();
					
				List<ImagemProdutoDTO> dtosimages = new ArrayList<ImagemProdutoDTO>();
				List<ImagemProduto> imagemProdutos = imagemProdutoRepository.buscaImagemProduto(id);
				
				for (ImagemProduto imagemProduto : imagemProdutos) {
					
					ImagemProdutoDTO imagemProdutoDTO = new ImagemProdutoDTO();
					imagemProdutoDTO.setId(imagemProduto.getId());
					imagemProdutoDTO.setEmpresa(imagemProduto.getEmpresa().getId());
					imagemProdutoDTO.setProduto(imagemProduto.getProduto().getId());
					//imagemProdutoDTO.setImagemOriginal(imagemProduto.getImagemOriginal());
					//imagemProdutoDTO.setImagemMiniatura(imagemProduto.getImagemMiniatura());
					
					dtosimages.add(imagemProdutoDTO);
				
				}
			
					ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
					itemVendaDTO.setQuantidade(item.getQuantidade());
					ProdutoDTO produtoDTO = new ProdutoDTO();
					itemVendaDTO.setId(item.getId());
					itemVendaDTO.setProduto(produtoDTO);
					produtoDTO.setId(item.getProduto().getId());
					produtoDTO.setNome(item.getProduto().getNome());
					produtoDTO.setDescricao(item.getProduto().getDescricao());
					produtoDTO.setPeso(item.getProduto().getPeso());
					produtoDTO.setLargura(item.getProduto().getLargura());
					produtoDTO.setAltura(item.getProduto().getAltura());
					produtoDTO.setProfundidade(item.getProduto().getProfundidade());
					produtoDTO.setValorVenda(item.getProduto().getValorVenda());
					produtoDTO.setQuantidadeEstoque(item.getProduto().getQuantidadeEstoque());
					produtoDTO.setEmpresa(item.getProduto().getEmpresa());
					produtoDTO.setCategoriaProduto(item.getProduto().getCategoriaProduto());
					produtoDTO.setMarcaProduto(item.getProduto().getMarcaProduto());
					produtoDTO.setImagens(dtosimages);
					
					compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
			}
			
			dtosVendas.add(compraLojaVirtualDTO);
		}
		
		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(dtosVendas, HttpStatus.OK);
	}
	
	
	@ResponseBody
	@GetMapping(value = "/consultaVendaProdutoId/{id}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>> consultaVendaProdutoId(@PathVariable("id") Long idProduto) {
		
		List<VendaCompraLojaVirtual> compraLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorProduto(idProduto);
		
		if (compraLojaVirtual == null) {
			compraLojaVirtual = new ArrayList<VendaCompraLojaVirtual>();
		}
		
		List<VendaCompraLojaVirtualDTO> dtosVendas = new ArrayList<VendaCompraLojaVirtualDTO>();
		
		for (VendaCompraLojaVirtual vendaCompraLojaVirtual : compraLojaVirtual) {
				
			VendaCompraLojaVirtualDTO compraLojaVirtualDTO =  new VendaCompraLojaVirtualDTO();
			
			compraLojaVirtualDTO.setId(vendaCompraLojaVirtual.getId());
			
			compraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtual.getValorTotal());
			compraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoa());
			
			compraLojaVirtualDTO.setEntrega(vendaCompraLojaVirtual.getEnderecoEntrega());
			compraLojaVirtualDTO.setCobranca(vendaCompraLojaVirtual.getEnderecoCobranca());
			
			compraLojaVirtualDTO.setValorDesconto(vendaCompraLojaVirtual.getValorDesconto());
			compraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtual.getValorFrete());
			
			compraLojaVirtualDTO.setFormaPagamento(vendaCompraLojaVirtual.getFormaPagamento());
			
			Long id = null;
			
			for (ItemVendaLoja item: vendaCompraLojaVirtual.getItemVendaLojas()) {
	
				id = item.getProduto().getId();
					
				List<ImagemProdutoDTO> dtosimages = new ArrayList<ImagemProdutoDTO>();
				List<ImagemProduto> imagemProdutos = imagemProdutoRepository.buscaImagemProduto(id);
				
				for (ImagemProduto imagemProduto : imagemProdutos) {
					
					ImagemProdutoDTO imagemProdutoDTO = new ImagemProdutoDTO();
					imagemProdutoDTO.setId(imagemProduto.getId());
					imagemProdutoDTO.setEmpresa(imagemProduto.getEmpresa().getId());
					imagemProdutoDTO.setProduto(imagemProduto.getProduto().getId());
					imagemProdutoDTO.setImagemOriginal(imagemProduto.getImagemOriginal());
					imagemProdutoDTO.setImagemMiniatura(imagemProduto.getImagemMiniatura());
					
					dtosimages.add(imagemProdutoDTO);
				
				}
			
					ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
					itemVendaDTO.setQuantidade(item.getQuantidade());
					ProdutoDTO produtoDTO = new ProdutoDTO();
					itemVendaDTO.setId(item.getId());
					itemVendaDTO.setProduto(produtoDTO);
					produtoDTO.setId(item.getProduto().getId());
					produtoDTO.setNome(item.getProduto().getNome());
					produtoDTO.setDescricao(item.getProduto().getDescricao());
					produtoDTO.setPeso(item.getProduto().getPeso());
					produtoDTO.setLargura(item.getProduto().getLargura());
					produtoDTO.setAltura(item.getProduto().getAltura());
					produtoDTO.setProfundidade(item.getProduto().getProfundidade());
					produtoDTO.setValorVenda(item.getProduto().getValorVenda());
					produtoDTO.setQuantidadeEstoque(item.getProduto().getQuantidadeEstoque());
					produtoDTO.setEmpresa(item.getProduto().getEmpresa());
					produtoDTO.setCategoriaProduto(item.getProduto().getCategoriaProduto());
					produtoDTO.setMarcaProduto(item.getProduto().getMarcaProduto());
					produtoDTO.setImagens(dtosimages);
					
					compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
			}
			
			dtosVendas.add(compraLojaVirtualDTO);
		}
		
		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(dtosVendas, HttpStatus.OK);
	}
	
	
	
	@ResponseBody
	@DeleteMapping(value = "/deletaVendaTotal/{idVenda}")
	public ResponseEntity<String> deletaVendaTotal(@PathVariable(value = "idVenda") Long idVenda) {
		
		
		if (!vendaCompraLojaVirtualRepository.findById(idVenda).isPresent()) {
			return new ResponseEntity<String>("Venda já foi removida.", HttpStatus.NOT_FOUND);
		}
		
		vendaService.exclusaoTotalVendaBanco(idVenda);
		
		return new ResponseEntity<String>("Venda excluida com sucesso!", HttpStatus.OK);
	}
	
	@ResponseBody
	@DeleteMapping(value = "/deletaVendaLogica/{idVenda}")
	public ResponseEntity<String> deletaVendaLogica(@PathVariable(value = "idVenda") Long idVenda) {
		
		
		if (!vendaCompraLojaVirtualRepository.findById(idVenda).isPresent()) {
			return new ResponseEntity<String>("Venda já foi removida.", HttpStatus.NOT_FOUND);
		}
		
		vendaService.exclusaoVendaLogica(idVenda);
		
		return new ResponseEntity<String>("Venda logica excluida com sucesso!", HttpStatus.OK);
	}
	
	@ResponseBody
	@PutMapping(value = "/ativaRegistroVendaBanco/{idVenda}")
	public ResponseEntity<String> ativaRegistroVendaBanco(@PathVariable(value = "idVenda") Long idVenda) {
		
		
		if (!vendaCompraLojaVirtualRepository.findById(idVenda).isPresent()) {
			return new ResponseEntity<String>("Venda já foi removida.", HttpStatus.NOT_FOUND);
		}
		
		vendaService.ativaRegistroVendaBanco(idVenda);
		
		return new ResponseEntity<String>("Registro venda atualizado com sucesso!", HttpStatus.OK);
	}
	
}
