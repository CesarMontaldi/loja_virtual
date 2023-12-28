package cesar.montaldi.lojavirtual;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import cesar.montaldi.lojavirtual.model.dto.ObjectErrorDTO;
import cesar.montaldi.lojavirtual.service.ServiceSendEmail;

@RestControllerAdvice
@ControllerAdvice
public class ControllerExceptions extends ResponseEntityExceptionHandler {
	
	@Autowired
	private ServiceSendEmail serviceSendEmail;
	
	@ExceptionHandler(ExceptionLojaVirtual.class)
	public ResponseEntity<Object> handleExceptionCuston(ExceptionLojaVirtual ex) {
		ObjectErrorDTO objectErrorDTO = new ObjectErrorDTO();
		
		objectErrorDTO.setError(ex.getMessage());
		objectErrorDTO.setCode(HttpStatus.OK.toString());
		
		return new ResponseEntity<Object>(objectErrorDTO, HttpStatus.OK);
	}
	
	/*Captura exceções do projeto*/
	@ExceptionHandler({Exception.class, RuntimeException.class, Throwable.class})
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		ObjectErrorDTO objectErrorDTO = new ObjectErrorDTO();
		
		String msg = "";
		
		if (ex instanceof MethodArgumentNotValidException) {
			List<ObjectError> list = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors();
			
			for (ObjectError objectError : list) {
				
				msg += objectError.getDefaultMessage() + "\n";
			}
		} 
		else if (ex instanceof HttpMessageNotReadableException) {
			
			msg = "Não está sendo enviado dados para o BODY corpo da requisição.";
		}
		else {
			msg = ex.getMessage();
		}
		
		objectErrorDTO.setError(msg);
		objectErrorDTO.setCode(status.value() + " ==> " + status.getReasonPhrase());
		
		ex.printStackTrace();
		
		try {
			serviceSendEmail.enviarEmailHtml("Erro na loja_virtual",
					ExceptionUtils.getStackTrace(ex),
					"guto_montaldi@yahoo.com.br");
			
		} catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<Object>(objectErrorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/*Captura erro na parte de banco de dados*/
	@ExceptionHandler({DataIntegrityViolationException.class, ConstraintViolationException.class, SQLException.class})
	protected ResponseEntity<Object> handleExceptionDataIntegry(Exception ex) {
		
		ObjectErrorDTO objectErrorDTO = new ObjectErrorDTO();
		
		String msg = "";
		
		if (ex instanceof DataIntegrityViolationException) {
			msg = "Erro de integridade no banco: " + ((DataIntegrityViolationException) ex).getCause().getCause().getMessage();
		}else 
			if (ex instanceof ConstraintViolationException) {
				msg = "Erro de chave estrangeira: " + ((ConstraintViolationException) ex).getCause().getCause().getMessage();
		}else 
			if (ex instanceof SQLException) {
				msg = "Erro de SQL no Banco: " + ((SQLException) ex).getCause().getCause().getMessage();
		}else {
			msg = ex.getMessage();
		}
		objectErrorDTO.setError(msg);
		objectErrorDTO.setCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
		
		ex.printStackTrace();
		
		try {
			serviceSendEmail.enviarEmailHtml("Erro na loja_virtual",
					ExceptionUtils.getStackTrace(ex),
					"guto_montaldi@yahoo.com.br");
			
		} catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<Object>(objectErrorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
				
		}
}

