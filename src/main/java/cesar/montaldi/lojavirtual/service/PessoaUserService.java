package cesar.montaldi.lojavirtual.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import cesar.montaldi.lojavirtual.model.PessoaFisica;
import cesar.montaldi.lojavirtual.model.PessoaJuridica;
import cesar.montaldi.lojavirtual.model.Usuario;
import cesar.montaldi.lojavirtual.repository.PessoaFisicaRepository;
import cesar.montaldi.lojavirtual.repository.PessoaJuridicaRepository;
import cesar.montaldi.lojavirtual.repository.UsuarioRepository;
import cesar.montaldi.lojavirtual.util.GeneratePassword;

@Service
public class PessoaUserService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
    private	PessoaJuridicaRepository pessoaJuridicaRepository;
	
	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ServiceSendEmail serviceSendEmail;
	
	public PessoaJuridica salvarPessoaJuridica(PessoaJuridica juridica ) {

		
		for (int i = 0; i < juridica.getEnderecos().size(); i++) {
			juridica.getEnderecos().get(i).setPessoa(juridica);
			juridica.getEnderecos().get(i).setEmpresa(juridica);
		}
		
		juridica = pessoaJuridicaRepository.save(juridica);
		
		Usuario usuarioPj = usuarioRepository.findUserByPessoa(juridica.getId(), juridica.getEmail());
		
		if (usuarioPj == null) {
			
			String constraint = usuarioRepository.consultaConstraintAcesso();
			if (constraint != null) {
				jdbcTemplate.execute("begin; alter table usuarios_acesso drop constraint " + constraint +"; commit;");
			}
			
			usuarioPj = new Usuario();
			usuarioPj.setDataAtualSenha(Calendar.getInstance().getTime());
			usuarioPj.setEmpresa(juridica);
			usuarioPj.setPessoa(juridica);
			usuarioPj.setLogin(juridica.getEmail());
		
			
			String senha = GeneratePassword.generateRandomPassword(15);
			String senhaCript = new BCryptPasswordEncoder().encode(senha);
			
			usuarioPj.setSenha(senhaCript);
			
			usuarioPj = usuarioRepository.save(usuarioPj);
			
			usuarioRepository.insertAcessoUser(usuarioPj.getId());
			usuarioRepository.insertAcessoUserPj(usuarioPj.getId(), "ROLE_ADMIN");
			
			StringBuilder messagemHtml = new StringBuilder();
			
			messagemHtml.append("<b>Olá </b>" +"<b>" + juridica.getNome().split(" ")[0] + ",</b> <br/>");
			messagemHtml.append("<b>Segue abaixo seus dados de acesso para a loja virtual.</b>" + "<br/>");
			messagemHtml.append("<b>Login: </b>" + juridica.getEmail() + "<br/>");
			messagemHtml.append("<b>Senha: </b>").append(senha).append("<br/>");
			messagemHtml.append("Obrigado!");
			
			/*Fazer o envio de e-mail do login e senha*/
			try {
				serviceSendEmail.enviarEmailHtml("Acesso gerado para Loja Virtual", messagemHtml.toString(), juridica.getEmail());
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return juridica;
		
	}

	public PessoaFisica salvarPessoaFisica(PessoaFisica pessoaFisica) {
		
		for (int i = 0; i < pessoaFisica.getEnderecos().size(); i++) {
			pessoaFisica.getEnderecos().get(i).setPessoa(pessoaFisica);
			//pessoaFisica.getEnderecos().get(i).setEmpresa(pessoaFisica);
		}
		
		pessoaFisica = pessoaFisicaRepository.save(pessoaFisica);
		
		Usuario usuarioPf = usuarioRepository.findUserByPessoa(pessoaFisica.getId(), pessoaFisica.getEmail());
		
		if (usuarioPf == null) {
			
			String constraint = usuarioRepository.consultaConstraintAcesso();
			if (constraint != null) {
				jdbcTemplate.execute("begin; alter table usuarios_acesso drop constraint " + constraint +"; commit;");
			}
			
			usuarioPf = new Usuario();
			usuarioPf.setDataAtualSenha(Calendar.getInstance().getTime());
			usuarioPf.setEmpresa(pessoaFisica.getEmpresa());
			usuarioPf.setPessoa(pessoaFisica);
			usuarioPf.setLogin(pessoaFisica.getEmail());
		
			
			String senha = GeneratePassword.generateRandomPassword(15);
			String senhaCript = new BCryptPasswordEncoder().encode(senha);
			
			usuarioPf.setSenha(senhaCript);
			
			usuarioPf = usuarioRepository.save(usuarioPf);
			
			usuarioRepository.insertAcessoUser(usuarioPf.getId());
	
			StringBuilder messagemHtml = new StringBuilder();
			
			messagemHtml.append("<b>Olá </b>" +"<b>" + pessoaFisica.getNome().split(" ")[0] + ",</b> <br/>");
			messagemHtml.append("<b>Segue abaixo seus dados de acesso para a loja virtual.</b>" + "<br/>");
			messagemHtml.append("<b>Login: </b>" + pessoaFisica.getEmail() + "<br/>");
			messagemHtml.append("<b>Senha: </b>").append(senha).append("<br/>");
			messagemHtml.append("Obrigado!");
			
			/*Fazer o envio de e-mail do login e senha*/
			try {
				serviceSendEmail.enviarEmailHtml("Acesso gerado para Loja Virtual", messagemHtml.toString(), pessoaFisica.getEmail());
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return pessoaFisica;
	}

}
