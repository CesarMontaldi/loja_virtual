package cesar.montaldi.lojavirtual;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import cesar.montaldi.lojavirtual.controller.PessoaController;
import cesar.montaldi.lojavirtual.model.PessoaJuridica;
import junit.framework.TestCase;

@Profile("test")
@SpringBootTest(classes= LojaVirtualApplication.class)
public class TestePessoaUsuario extends TestCase {

	@Autowired
	private PessoaController pessoaController;
	
	@Test
	public void testCadastroPessoa() throws ExceptionLojaVirtual {
		
		PessoaJuridica pessoaJuridica = new PessoaJuridica();
		
		pessoaJuridica.setNome("Cesar Montaldi");
		pessoaJuridica.setCnpj("" + Math.random());
		pessoaJuridica.setEmail("cesar.montaldi@yahoo.com");
		pessoaJuridica.setTelefone("19998745821");
		pessoaJuridica.setinscricaoEstadual("3258962581");
		pessoaJuridica.setinscricaoMunicipal("95135785236");
		pessoaJuridica.setNomeFantasia("Montaldi Informatica");
		pessoaJuridica.setRazaoSocial("7895214796320");
		

		pessoaController.salvarPj(pessoaJuridica);
		
		/*
		PessoaFisica pessoaFisica = new PessoaFisica();
		
		pessoaFisica.setNome("Cesar Montaldi");
		pessoaFisica.setCpf("32158712358");
		pessoaFisica.setEmail("cesar.montaldi@gmail.com");
		pessoaFisica.setTelefone("19998745821");
		pessoaFisica.setEmpresa(pessoaFisica); */
		
	}

}
