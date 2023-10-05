package cesar.montaldi.lojavirtual;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import cesar.montaldi.lojavirtual.controller.AcessoController;
import cesar.montaldi.lojavirtual.model.Acesso;
import cesar.montaldi.lojavirtual.repository.AcessoRepository;
import cesar.montaldi.lojavirtual.service.AcessoService;

@SpringBootTest(classes= LojaVirtualApplication.class)
public class LojaVirtualApplicationTests {
	
	@Autowired
	private AcessoService acessoService;
	
//	@Autowired
//	private AcessoRepository acessoRepository;
	
	@Autowired
	private AcessoController acessoController;

	@Test
	public void testCadastroAcesso() {
		
		Acesso acesso = new Acesso();
		
		acesso.setDescricao("ROLE_ADMIN");
		
		acessoController.salvarAcesso(acesso);
	}

}
