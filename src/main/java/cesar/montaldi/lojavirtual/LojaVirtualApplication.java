package cesar.montaldi.lojavirtual;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EntityScan(basePackages = "cesar.montaldi.lojavirtual.model")
@ComponentScan(basePackages = {"cesar.*"})
@EnableJpaRepositories(basePackages = {"cesar.montaldi.lojavirtual.repository"})
@EnableTransactionManagement
public class LojaVirtualApplication {

	public static void main(String[] args) {
		
		System.out.println(new BCryptPasswordEncoder().encode("123"));
		
		SpringApplication.run(LojaVirtualApplication.class, args);
	}

}
