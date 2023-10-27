package cesar.montaldi.lojavirtual;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import cesar.montaldi.lojavirtual.controller.PessoaController;
import cesar.montaldi.lojavirtual.enums.TipoEndereco;
import cesar.montaldi.lojavirtual.model.Endereco;
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
		
		Endereco endereco1 = new Endereco();
		endereco1.setLogradouro("Av. Cabo Pedro Hoffman");
		endereco1.setBairro("Real Parque");
		endereco1.setCep("13178574");
		endereco1.setCidade("Sumare");
		endereco1.setNumero("55");
		endereco1.setUf("SP");
		endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
		endereco1.setPessoa(pessoaJuridica);
		endereco1.setEmpresa(pessoaJuridica);
		
		Endereco endereco2 = new Endereco();
		endereco2.setLogradouro("Rua Ceara");
		endereco2.setBairro("Nova Veneza");
		endereco2.setCep("13177160");
		endereco2.setCidade("Sumare");
		endereco2.setNumero("463");
		endereco2.setUf("SP");
		endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
		endereco2.setPessoa(pessoaJuridica);
		endereco2.setEmpresa(pessoaJuridica);
		
		pessoaJuridica.getEnderecos().add(endereco1);
		pessoaJuridica.getEnderecos().add(endereco2);

		pessoaJuridica = pessoaController.salvarPj(pessoaJuridica).getBody();
		
		assertEquals(true, pessoaJuridica.getId() > 0);
		
		for (Endereco endereco : pessoaJuridica.getEnderecos()) {
			assertEquals(true, pessoaJuridica.getId() > 0);
		}
		
		assertEquals(2, pessoaJuridica.getEnderecos().size());
		
		/*
		PessoaFisica pessoaFisica = new PessoaFisica();
		
		pessoaFisica.setNome("Cesar Montaldi");
		pessoaFisica.setCpf("32158712358");
		pessoaFisica.setEmail("cesar.montaldi@gmail.com");
		pessoaFisica.setTelefone("19998745821");
		pessoaFisica.setEmpresa(pessoaFisica); */
		
	}

}
