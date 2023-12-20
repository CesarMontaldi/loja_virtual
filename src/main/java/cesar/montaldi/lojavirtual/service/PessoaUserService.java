package cesar.montaldi.lojavirtual.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import cesar.montaldi.lojavirtual.model.PessoaJuridica;
import cesar.montaldi.lojavirtual.model.Usuario;
import cesar.montaldi.lojavirtual.repository.PessoaRepository;
import cesar.montaldi.lojavirtual.repository.UsuarioRepository;

@Service
public class PessoaUserService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
    private	PessoaRepository pessoaRepository;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ServiceSendEmail serviceSendEmail;
	
	public PessoaJuridica salvarPessoaJuridica(PessoaJuridica juridica ) {

		
		for (int i = 0; i < juridica.getEnderecos().size(); i++) {
			juridica.getEnderecos().get(i).setPessoa(juridica);
			juridica.getEnderecos().get(i).setEmpresa(juridica);
		}
		
		juridica = pessoaRepository.save(juridica);
		
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
			
			usuarioRepository.insertAcessoUserPj(usuarioPj.getId());
			//usuarioRepository.insertAcessoUserPj(usuarioPj.getId(), "ROLE_ADMIN");
			
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

}
