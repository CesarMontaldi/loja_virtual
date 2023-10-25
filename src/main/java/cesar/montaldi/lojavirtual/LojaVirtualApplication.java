package cesar.montaldi.lojavirtual;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import cesar.montaldi.lojavirtual.service.GeneratePassword;


@SpringBootApplication
@EntityScan(basePackages = "cesar.montaldi.lojavirtual.model")
@ComponentScan(basePackages = {"cesar.*"})
@EnableJpaRepositories(basePackages = {"cesar.montaldi.lojavirtual.repository"})
@EnableTransactionManagement
public class LojaVirtualApplication {
	
	public static void main(String[] args) {
		
		SpringApplication.run(LojaVirtualApplication.class, args);

		/*
		String senha = GeneratePassword.generateRandomPassword(15);
		String senhaCript = new BCryptPasswordEncoder().encode(senha);
		System.out.println("Senha =>: " + senha);
		System.out.println("SenhaCript =>: " + senhaCript);*/
	}
	
}
