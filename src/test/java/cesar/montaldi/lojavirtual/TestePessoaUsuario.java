package cesar.montaldi.lojavirtual;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import cesar.montaldi.lojavirtual.controller.PessoaController;
import cesar.montaldi.lojavirtual.enums.TipoEndereco;
import cesar.montaldi.lojavirtual.model.Endereco;
import cesar.montaldi.lojavirtual.model.PessoaFisica;
import cesar.montaldi.lojavirtual.model.PessoaJuridica;
import cesar.montaldi.lojavirtual.repository.PessoaJuridicaRepository;
import junit.framework.TestCase;

@Profile("test")
@SpringBootTest(classes= LojaVirtualApplication.class)
public class TestePessoaUsuario extends TestCase {

	@Autowired
	private PessoaController pessoaController;
	
	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;
	
	@Test
	public void testCadastroPessoaJuridica() throws ExceptionLojaVirtual {
		
		PessoaJuridica pessoaJuridica = new PessoaJuridica();
		pessoaJuridica.setCnpj("" + Math.random());
		pessoaJuridica.setNome("Cesar Augusto Montaldi");
		pessoaJuridica.setEmail("guto_montaldi@yahoo.com.br");
		pessoaJuridica.setTelefone("19998745821");
		pessoaJuridica.setinscricaoEstadual("325896258");
		pessoaJuridica.setinscricaoMunicipal("9513578523");
		pessoaJuridica.setNomeFantasia("Montaldi Informatica");
		pessoaJuridica.setRazaoSocial("789521479630");
		
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
			assertEquals(true, endereco.getId() > 0);
		}
		
		assertEquals(2, pessoaJuridica.getEnderecos().size());

		
	}
	
	@Test
	public void testCadastroPessoaFisica() throws ExceptionLojaVirtual {
		
		PessoaJuridica pessoaJuridica = pessoaJuridicaRepository.existeCnpjCadastrado("0.6290275524108812");
		
		PessoaFisica pessoaFisica = new PessoaFisica();
		pessoaFisica.setCpf("066.772.240-82");
		pessoaFisica.setNome("Cesar Montaldi");
		pessoaFisica.setEmail("guto_montaldi@yahoo.com.br");
		pessoaFisica.setTelefone("19998745821");
		pessoaFisica.setEmpresa(pessoaJuridica);
		
		Endereco endereco1 = new Endereco();
		endereco1.setLogradouro("Av. Cabo Pedro Hoffman");
		endereco1.setBairro("Real Parque");
		endereco1.setCep("13178574");
		endereco1.setCidade("Sumare");
		endereco1.setNumero("55");
		endereco1.setUf("SP");
		endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
		endereco1.setPessoa(pessoaFisica);
		endereco1.setEmpresa(pessoaJuridica);
		
		Endereco endereco2 = new Endereco();
		endereco2.setLogradouro("Rua Ceara");
		endereco2.setBairro("Nova Veneza");
		endereco2.setCep("13177160");
		endereco2.setCidade("Sumare");
		endereco2.setNumero("463");
		endereco2.setUf("SP");
		endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
		endereco2.setPessoa(pessoaFisica);
		endereco2.setEmpresa(pessoaJuridica);
		
		pessoaFisica.getEnderecos().add(endereco1);
		pessoaFisica.getEnderecos().add(endereco2);

		pessoaFisica = pessoaController.salvarPf(pessoaFisica).getBody();
		
		assertEquals(true, pessoaFisica.getId() > 0);
		
		for (Endereco endereco : pessoaFisica.getEnderecos()) {
			assertEquals(true, endereco.getId() > 0);
		}
		
		assertEquals(2, pessoaFisica.getEnderecos().size());
		
	}

}
