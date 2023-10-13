package cesar.montaldi.lojavirtual.service;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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
		
		/*Usado para ver no Postman para testes*/
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
	}
}
