package cesar.montaldi.lojavirtual.controller;

import java.util.ArrayList;
import java.util.List;

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

import cesar.montaldi.lojavirtual.model.ImagemProduto;
import cesar.montaldi.lojavirtual.model.dto.ImagemProdutoDTO;
import cesar.montaldi.lojavirtual.repository.ImagemProdutoRepository;

@RestController
public class ImagemProdutoController {

	@Autowired
	private ImagemProdutoRepository imagemProdutoRepository;
	
	
	@ResponseBody
	@PostMapping("/salvarImagemProduto")
	public ResponseEntity<ImagemProdutoDTO> salvarImagemProduto(@RequestBody ImagemProduto imagemProduto) {
		
		imagemProduto = imagemProdutoRepository.saveAndFlush(imagemProduto);
		
		ImagemProdutoDTO imagemProdutoDTO = new ImagemProdutoDTO();
		imagemProdutoDTO.setId(imagemProduto.getId());
		imagemProdutoDTO.setEmpresa(imagemProduto.getEmpresa().getId());
		imagemProdutoDTO.setProduto(imagemProduto.getProduto().getId());
		imagemProdutoDTO.setImagemOriginal(imagemProduto.getImagemOriginal());
		imagemProdutoDTO.setImagemMiniatura(imagemProduto.getImagemMiniatura());
		
		return new ResponseEntity<ImagemProdutoDTO>(imagemProdutoDTO, HttpStatus.OK);
		
	}
	
	
	@ResponseBody
	@GetMapping("/buscaImagemPorProduto/{idProduto}")
	public ResponseEntity<List<ImagemProdutoDTO>> buscaImagemPorProduto(@PathVariable("idProduto") Long idProduto) {
	
		List<ImagemProdutoDTO> dtos = new ArrayList<ImagemProdutoDTO>();
		
		List<ImagemProduto> imagemProdutos = imagemProdutoRepository.buscaImagemProduto(idProduto);
		
		for (ImagemProduto imagemProduto : imagemProdutos) {
			ImagemProdutoDTO imagemProdutoDTO = new ImagemProdutoDTO();
			imagemProdutoDTO.setId(imagemProduto.getId());
			imagemProdutoDTO.setEmpresa(imagemProduto.getEmpresa().getId());
			imagemProdutoDTO.setProduto(imagemProduto.getProduto().getId());
			imagemProdutoDTO.setImagemOriginal(imagemProduto.getImagemOriginal());
			imagemProdutoDTO.setImagemMiniatura(imagemProduto.getImagemMiniatura());
			
			dtos.add(imagemProdutoDTO);
		}
		
		return new ResponseEntity<List<ImagemProdutoDTO>>(dtos, HttpStatus.OK);
	}
	
	
	@ResponseBody
	@DeleteMapping("/deleteImagemProduto")
	public ResponseEntity<?> deleteImagemProduto(@RequestBody ImagemProduto imagemProduto) {

		if (!imagemProdutoRepository.findById(imagemProduto.getId()).isPresent()) {
			return new ResponseEntity<String>("Imagem do Produto já foi removida ou não existe com o id: " + imagemProduto, HttpStatus.NOT_FOUND);
		}
		
		imagemProdutoRepository.deleteById(imagemProduto.getId());
		
		return new ResponseEntity<String>("Imagem do Produto Removida.", HttpStatus.OK);
	}
	
	@ResponseBody
	@DeleteMapping("/deleteImagemProdutoId/{id}")
	public ResponseEntity<?> deleteImagemProdutoId(@PathVariable("id") Long id) {

		if (!imagemProdutoRepository.existsById(id)) {
			return new ResponseEntity<String>("Imagem do Produto já foi removida.", HttpStatus.NOT_FOUND);
		}
		
		imagemProdutoRepository.deleteById(id);
		
		return new ResponseEntity<String>("Imagem do Produto Removida.", HttpStatus.OK);
	}
	
	@ResponseBody
	@DeleteMapping("/deleteAllImagemsProduto/{idProduto}")
	public ResponseEntity<?> deleteAllImagemsProduto(@PathVariable("idProduto") Long idProduto) {

		if (!imagemProdutoRepository.findById(idProduto).isPresent()) {
			return new ResponseEntity<String>("Imagems do Produto já forom removidas.", HttpStatus.NOT_FOUND);
		}
		
		imagemProdutoRepository.deleteImagens(idProduto);
		
		return new ResponseEntity<String>("Imagems do Produto Removidas.", HttpStatus.OK);
	}

}
