package cesar.montaldi.lojavirtual.controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;

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
import cesar.montaldi.lojavirtual.model.ImagemProduto;
import cesar.montaldi.lojavirtual.model.Produto;
import cesar.montaldi.lojavirtual.model.dto.ImagemProdutoDTO;
import cesar.montaldi.lojavirtual.model.dto.ProdutoDTO;
import cesar.montaldi.lojavirtual.repository.ImagemProdutoRepository;
import cesar.montaldi.lojavirtual.repository.ProdutoRepository;
import cesar.montaldi.lojavirtual.service.ServiceSendEmail;

@Controller
@RestController 
public class ProdutoController {
	
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ServiceSendEmail serviceSendEmail;
	
	@Autowired
	ImagemProdutoRepository imagemProdutoRepository;

	@ResponseBody /*Poder dar um retorno da API*/
	@PostMapping(value = "/salvarProduto")/*Mapeando a url para receber JSON*/
	public ResponseEntity<Produto> salvarProduto(@RequestBody @Valid Produto produto) throws ExceptionLojaVirtual, MessagingException, IOException {/*Recebe o JSON e converte para Objeto*/
		

		if (produto.getEmpresa() == null || produto.getEmpresa().getId() <= 0) {
			throw new ExceptionLojaVirtual("Empresa responsável deve ser informada.");
		}
		
		if (produto.getTipoUnidade() == null || produto.getTipoUnidade() .trim().isEmpty()) {
			throw new ExceptionLojaVirtual("O tipo da unidade deve ser informada.");
		}
		
		if (produto.getNome().length() < 10) {
			throw new ExceptionLojaVirtual("O nome do produto deve ter mais de 10 letras.");
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
		
		if (produto.getQuantidadeEstoque() < 1) {
			throw new ExceptionLojaVirtual("O produto deve ter no minimo 1 no estoque.");
		}
		
		if (produto.getImagens() == null || produto.getImagens().isEmpty() || produto.getImagens().size() == 0) {
			throw new ExceptionLojaVirtual("Deve ser informado imagens para o produto.");
		}
		
		if (produto.getImagens().size() < 3) {
			throw new ExceptionLojaVirtual("Deve ser informado pelo menos 3 imagens para o produto.");
		}
		
		if (produto.getImagens().size() > 6 ) {
			throw new ExceptionLojaVirtual("Deve ser informado no máximo 6 imagens.");
		}
		
		if (produto.getId() == null) {
			
			for (int i = 0; i < produto.getImagens().size(); i++) {
				produto.getImagens().get(i).setProduto(produto);
				produto.getImagens().get(i).setEmpresa(produto.getEmpresa()); 
				
				String base64Image = "";
				
				if (produto.getImagens().get(i).getImagemOriginal().contains("data:image")) {
					base64Image = produto.getImagens().get(i).getImagemOriginal().split(",")[1];
				} else {
					base64Image = produto.getImagens().get(i).getImagemOriginal();
				}
				
				byte[] imagesBytes = DatatypeConverter.parseBase64Binary(base64Image);
				
				BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imagesBytes));
				
				if (bufferedImage != null) {
					
					int type = bufferedImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
					int largura = Integer.parseInt("800");
					int altura = Integer.parseInt("600");
					
					BufferedImage resizedImage = new BufferedImage(largura, altura, type);
					Graphics2D graphics2d = resizedImage.createGraphics();
					graphics2d.drawImage(bufferedImage, 0, 0, largura, altura, null);
					graphics2d.dispose();
					
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					ImageIO.write(resizedImage, "png", byteArrayOutputStream);
					
					String miniImageBase64 = "data:image/png;base64," + DatatypeConverter.printBase64Binary(byteArrayOutputStream.toByteArray());
					
					produto.getImagens().get(i).setImagemMiniatura(miniImageBase64);
					
					bufferedImage.flush();
					resizedImage.flush();
					byteArrayOutputStream.flush();
					byteArrayOutputStream.close();
				}
			}
		}

		Produto produtoSalvo = produtoRepository.save(produto);
		
		if (produto.getAlertaQuantidadeEstoque() && produto.getQuantidadeEstoque() <= 1) {
			
			StringBuilder html = new StringBuilder();
			html.append("<h2>")
			.append("Produto: " + produto.getNome())
			.append(" com estoque baixo: </h2>" + produto.getQuantidadeEstoque());
			html.append("<p> Id Produto: ").append(produto.getId()).append("</p>");
			
			if (produto.getEmpresa().getEmail() != null) {
				serviceSendEmail.enviarEmailHtml("Produto sem estoque", html.toString(), produto.getEmpresa().getEmail());
			}
			
		}
		
		return new ResponseEntity<Produto>(produtoSalvo, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/buscaProdutoNome/{nome}")
	public ResponseEntity<List<ProdutoDTO>> buscaProdutoNome(@PathVariable String nome) throws ExceptionLojaVirtual {
		
		
		List<Produto> produtos = produtoRepository.buscarProdutoPorNome(nome.toUpperCase());

		
		if (produtos.isEmpty()) {
			throw new ExceptionLojaVirtual("Não foi possível encontrar o Produto.");
		}
		
		
		List<ProdutoDTO> produtoDto = new ArrayList<ProdutoDTO>();
		Long id = null;
		
		
			for (int i = 0; i < produtos.size(); i++) {
				id = produtos.get(i).getId();
				
				List<ImagemProdutoDTO> imagenDto = new ArrayList<ImagemProdutoDTO>();
				List<ImagemProduto> imagemProdutos = imagemProdutoRepository.buscaImagemProduto(id);
				
					for (ImagemProduto imagemProduto : imagemProdutos) {
						
						
						ImagemProdutoDTO imagemProdutoDTO = new ImagemProdutoDTO();
						imagemProdutoDTO.setId(imagemProduto.getId());
						imagemProdutoDTO.setEmpresa(imagemProduto.getEmpresa().getId());
						imagemProdutoDTO.setProduto(imagemProduto.getProduto().getId());
						imagemProdutoDTO.setImagemOriginal(imagemProduto.getImagemOriginal());
						imagemProdutoDTO.setImagemMiniatura(imagemProduto.getImagemMiniatura());
						
						imagenDto.add(imagemProdutoDTO);
					
				}
			
						ProdutoDTO produtoDTO = new ProdutoDTO();
						produtoDTO.setId(produtos.get(i).getId());
						produtoDTO.setNome(produtos.get(i).getNome());
						produtoDTO.setDescricao(produtos.get(i).getDescricao());
						produtoDTO.setPeso(produtos.get(i).getPeso());
						produtoDTO.setLargura(produtos.get(i).getLargura());
						produtoDTO.setAltura(produtos.get(i).getAltura());
						produtoDTO.setProfundidade(produtos.get(i).getProfundidade());
						produtoDTO.setValorVenda(produtos.get(i).getValorVenda());
						produtoDTO.setQuantidadeAlertaEstoque(produtos.get(i).getQuantidadeAlertaEstoque());
						produtoDTO.setQuantidadeEstoque(produtos.get(i).getQuantidadeEstoque());
						produtoDTO.setLinkYoutube(produtos.get(i).getLinkYoutube());
						produtoDTO.setAlertaQuantidadeEstoque(produtos.get(i).getAlertaQuantidadeEstoque());
						produtoDTO.setQuantidadeClique(produtos.get(i).getQuantidadeClique());
						produtoDTO.setEmpresa(produtos.get(i).getEmpresa());
						produtoDTO.setCategoriaProduto(produtos.get(i).getCategoriaProduto());
						produtoDTO.setMarcaProduto(produtos.get(i).getMarcaProduto());
						produtoDTO.setImagens(imagenDto);
					
					
						produtoDto.add(produtoDTO);
			}
				

		return new ResponseEntity<List<ProdutoDTO>>(produtoDto, HttpStatus.OK);
	}
	

	@ResponseBody
	@GetMapping(value = "/buscaProdutoId/{id}")
	public ResponseEntity<?> buscaProdutoId(@PathVariable("id") Long id) throws ExceptionLojaVirtual {
		
		
		Produto produto = produtoRepository.findById(id).orElse(null);
		
		if (produto == null) {
			throw new ExceptionLojaVirtual("Não foi possível encontrar o Produto com o código: " + id);
		}

		
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
	
			ProdutoDTO produtoDTO = new ProdutoDTO();
			produtoDTO.setId(produto.getId());
			produtoDTO.setNome(produto.getNome());
			produtoDTO.setDescricao(produto.getDescricao());
			produtoDTO.setPeso(produto.getPeso());
			produtoDTO.setLargura(produto.getLargura());
			produtoDTO.setAltura(produto.getAltura());
			produtoDTO.setProfundidade(produto.getProfundidade());
			produtoDTO.setValorVenda(produto.getValorVenda());
			produtoDTO.setQuantidadeAlertaEstoque(produto.getQuantidadeAlertaEstoque());
			produtoDTO.setQuantidadeEstoque(produto.getQuantidadeEstoque());
			produtoDTO.setLinkYoutube(produto.getLinkYoutube());
			produtoDTO.setAlertaQuantidadeEstoque(produto.getAlertaQuantidadeEstoque());
			produtoDTO.setQuantidadeClique(produto.getQuantidadeClique());
			produtoDTO.setEmpresa(produto.getEmpresa());
			produtoDTO.setCategoriaProduto(produto.getCategoriaProduto());
			produtoDTO.setMarcaProduto(produto.getMarcaProduto());
			produtoDTO.setImagens(dtosimages);
				
			return new ResponseEntity<ProdutoDTO>(produtoDTO, HttpStatus.OK);
			
		}
	

	@ResponseBody 
	@PostMapping(value = "/deleteProduto")
	public ResponseEntity<?> deleteProduto(@RequestBody Produto produto) {

		if (!produtoRepository.findById(produto.getId()).isPresent()) {
			return new ResponseEntity<String>("Produto já foi removido.", HttpStatus.NOT_FOUND);
		}
		
		produtoRepository.deleteById(produto.getId());
		
		return new ResponseEntity<String>("Produto Removido", HttpStatus.OK);
	}
	
	
	@ResponseBody 
	@DeleteMapping(value = "/deleteProdutoId/{id}")
	public ResponseEntity<?> deleteProdutoId(@PathVariable("id") Long id) {
		
		if (!produtoRepository.findById(id).isPresent()) {
			return new ResponseEntity<String>("Produto já foi removido.", HttpStatus.NOT_FOUND);
		}
		
	    produtoRepository.deleteById(id);
	
		return new ResponseEntity<String>("Produto Removido", HttpStatus.OK);
	}
		
}
