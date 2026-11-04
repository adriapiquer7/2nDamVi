package SpringROA.SpringROA.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GestorErrors {

	@ExceptionHandler(JocException.class)
	public ResponseEntity<String> joc(JocException e) {
		return ResponseEntity.status(e.getStatus()).body(e.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> validacio(MethodArgumentNotValidException e) {
		Map<String, String> errors = new LinkedHashMap<>();
		for (FieldError fe : e.getBindingResult().getFieldErrors()) {
			errors.put(fe.getField(), fe.getDefaultMessage());
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}
}
