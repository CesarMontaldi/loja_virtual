package cesar.montaldi.lojavirtual;

import org.springframework.http.HttpStatus;

public class ExceptionLojaVirtual extends Exception {

	private static final long serialVersionUID = 1L;

	public ExceptionLojaVirtual(String msgError) {
		super(msgError);
	}

}
