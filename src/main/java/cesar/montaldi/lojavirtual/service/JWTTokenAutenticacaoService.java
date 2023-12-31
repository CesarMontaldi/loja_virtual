package cesar.montaldi.lojavirtual.service;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import cesar.montaldi.lojavirtual.ApplicationContextLoad;
import cesar.montaldi.lojavirtual.model.Usuario;
import cesar.montaldi.lojavirtual.repository.UsuarioRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

/*Criar a autenticação e retornar também a autenticação JWT*/
@Service
@Component
public class JWTTokenAutenticacaoService {
	
	/*Token de validade de 10 dias*/
	private static final long EXPIRATION_TIME = 869990000;
	
	/*Chave de senha para juntar com JWT*/
	private static final String SECRET = "7Dgz%\\d:D1NCF6EJ7088!?Km=$5C9$";
	
	private static final String TOKEN_PREFIX = "Bearer";
	
	private static final String HEADER_STRING = "Authorization";
	
	/*Gera o token e da a resposta para o cliente com o JWT*/
	public void addAuthentication(HttpServletResponse response, String username) throws Exception {
		
		/*Montagem do Token*/
		String JWT = Jwts.builder() /*Chama o gerador de token*/
				     .setSubject(username) /*Adiciona o user*/
				     .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) /*Tempo de expiração*/
				     .signWith(SignatureAlgorithm.HS512, SECRET).compact();
		
		String token = TOKEN_PREFIX + " " + JWT;
		
		/*Dá a resposta para tela, para o cliente, outra API, navegador, aplicativo, javascript, outra aplicação Java*/
		response.addHeader(HEADER_STRING, token);
		
		liberacaoCors(response);
		
		/*Usado para ver no Postman para testes*/
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
	}
	
	/*Retorna o usuário validado com token ou caso não seja valido retorna null*/
	public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String token = request.getHeader(HEADER_STRING);
		
		try {
		
			if (token != null) {
				
				String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim();
				
				/*Faz a validação do token do usuário na requisição e obtem o USER*/
				String user = Jwts.parser()
						      .setSigningKey(SECRET)
						      .parseClaimsJws(tokenLimpo)
						      .getBody().getSubject();
				
				if (user != null) {
					
					Usuario usuario = ApplicationContextLoad
							          .getApplicationContext()
							          .getBean(UsuarioRepository.class).findUserByLogin(user);
					
					if (usuario != null) {
						return new UsernamePasswordAuthenticationToken(
								usuario.getLogin(),
								usuario.getSenha(),
								usuario.getAuthorities());
					}	
				}
			}
		/*Lança excessão caso a assinatura do token esteja errado*/
		}catch (SignatureException e) {
			response.getWriter().write("Token está inválido.");
		
		/*Lança excessão caso o token esteja expirado*/
		}catch (ExpiredJwtException e) {
			response.getWriter().write("Token está expirado, efetue o login novamente.");
		}
		
		finally {
			liberacaoCors(response);
		}

		return null;
	} 
	
	
	/*fazendo liberação contra erro de Cors no navegador*/
	private void liberacaoCors(HttpServletResponse response) {
		if (response.getHeader("Access-Control-Allow-Origin") == null) {
			response.addHeader("Access-Control-Allow-Origin", "*");
		}
		
		if (response.getHeader("Access-Control-Allow-Headers") == null) {
			response.addHeader("Access-Control-Allow-Headers", "*");
		}
		
		if (response.getHeader("Access-Control-Request-Headers") == null) {
			response.addHeader("Access-Control-Request-Headers", "*");
		}
		
		if (response.getHeader("Access-Control-Allow-Methods") == null) {
			response.addHeader("Access-Control-Allow-Methods", "*");
		}
	}
	
}





