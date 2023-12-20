package cesar.montaldi.lojavirtual.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import cesar.montaldi.lojavirtual.model.Usuario;
import cesar.montaldi.lojavirtual.repository.UsuarioRepository;

@Component
@Service
public class TarefaAutomatizadaService {
	
	@Autowired
	private ServiceSendEmail serviceSendEmail;
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Scheduled(initialDelay = 2000, fixedDelay = 86400000) /*Roda a cada 24 horas*/
	//@Scheduled(cron = "0 0 11 * * *", zone = "America/Sao_Paulo") /*Vai rodar todo dia as 11 horas da manhã, horario de São Paulo/Brasilia*/ 
	public void notificarUserTrocaSenha() throws UnsupportedEncodingException, MessagingException, InterruptedException {
		
		List<Usuario> usuarios = usuarioRepository.usuarioSenhaVencida();
		
		for (Usuario usuario : usuarios) {
			
			StringBuilder msg = new StringBuilder();
			msg.append("Olá, ").append(usuario.getPessoa().getNome()).append("<br/>");
			msg.append("Está na hora de troca sua senha, já passou 90 dias de validade.").append("<br/>");
			msg.append("Troque sua senha da Loja Virtual do César - Montaldi Store");
			
			serviceSendEmail.enviarEmailHtml("Troca de senha", msg.toString(), usuario.getLogin());
			
			Thread.sleep(3000);
		}
	}
}
