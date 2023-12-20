package cesar.montaldi.lojavirtual.util;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GeneratePassword {
	
	public static String generateRandomPassword(int len)
    {
        // intervalo ASCII – alfanumérico (0-9, a-z, A-Z)
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ@!#$%&>?;:<~^abcdefghijklmnopqrstuvwxyz0123456789";
 
        SecureRandom random = new SecureRandom();
 
        // cada iteração do loop escolhe aleatoriamente um caractere do dado
        // intervalo ASCII e o anexa à instância `StringBuilder`
        return IntStream.range(0, len)
                .map(i -> random.nextInt(chars.length()))
                .mapToObj(randomIndex -> String.valueOf(chars.charAt(randomIndex)))
                .collect(Collectors.joining());
    }

}
