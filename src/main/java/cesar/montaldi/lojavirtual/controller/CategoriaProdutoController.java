package cesar.montaldi.lojavirtual.controller;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cesar.montaldi.lojavirtual.ExceptionLojaVirtual;
import cesar.montaldi.lojavirtual.model.Acesso;
import cesar.montaldi.lojavirtual.model.CategoriaProduto;
import cesar.montaldi.lojavirtual.model.dto.CategoriaProdutoDto;
import cesar.montaldi.lojavirtual.repository.CategoriaProdutoRepository;

@RestController
public class CategoriaProdutoController {
	
	@Autowired
	private CategoriaProdutoRepository categoriaProdutoRepository;
	
	@ResponseBody
	@PostMapping(value = "/salvarCategoria")
	public ResponseEntity<CategoriaProdutoDto> salvarCategoria(@RequestBody CategoriaProduto categoriaProduto) throws ExceptionLojaVirtual {
		
		if (categoriaProduto.getEmpresa() == null || (categoriaProduto.getEmpresa().getId() == null)) {
			throw new ExceptionLojaVirtual("A empresa deve ser informada.");
		}
		
		if (categoriaProduto.getId() == null && categoriaProdutoRepository.existeCategoria(categoriaProduto.getNomeDescricao().toUpperCase())) {
			throw new ExceptionLojaVirtual("Não pode cadastrar categoria com o mesmo nome.");
		}
		
		CategoriaProduto categoriaSalva = categoriaProdutoRepository.save(categoriaProduto);
		
		CategoriaProdutoDto categoriaProdutoDto = new CategoriaProdutoDto();
		categoriaProdutoDto.setId(categoriaSalva.getId());
		categoriaProdutoDto.setNomeDescricao(categoriaSalva.getNomeDescricao());
		categoriaProdutoDto.setEmpresa(categoriaSalva.getEmpresa().getId().toString());
	
		return new ResponseEntity<CategoriaProdutoDto>(categoriaProdutoDto, HttpStatus.OK);
	}
	
	@ResponseBody
	@DeleteMapping(value = "/deletarCategoriaProduto/{id}")
	public ResponseEntity<?> deletarCategoriaProduto(@PathVariable("id") Long id) {
		
		if (!categoriaProdutoRepository.findById(id).isPresent()) {
			return new ResponseEntity("Categoria já foi removida.", HttpStatus.NOT_FOUND);
		} 
		
		categoriaProdutoRepository.deleteById(id);
		
		return new ResponseEntity("Categoria Removida.", HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/buscarCategoriaPorDesc/{desc}")
	public ResponseEntity<List<CategoriaProduto>> buscarCategoriaPorDesc(@PathVariable("desc") String desc) {
		
		List<CategoriaProduto> categorias = categoriaProdutoRepository.buscarCategoriaPorDescricao(desc.toUpperCase());
		
		if (categorias.isEmpty()) {
			return new ResponseEntity("Categoria não encontrada.", HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<List<CategoriaProduto>>(categorias, HttpStatus.OK);
	}
}
