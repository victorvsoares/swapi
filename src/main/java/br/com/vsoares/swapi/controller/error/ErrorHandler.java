package br.com.vsoares.swapi.controller.error;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.vsoares.swapi.controller.exception.RecursoNaoEncontradoException;
import lombok.extern.log4j.Log4j2;

@ControllerAdvice
@Log4j2
public class ErrorHandler {
 
	@Autowired
	private MessageSource messageSource;
   
	@ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<?> processValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<org.springframework.validation.FieldError> fieldErrors = result.getFieldErrors();

        return new ResponseEntity<>(processFieldErrors(fieldErrors), BAD_REQUEST);
    }
	
	@ExceptionHandler(RecursoNaoEncontradoException.class)
    @ResponseBody
    public ResponseEntity<Erro> processValidationError(RecursoNaoEncontradoException ex) {
        return new ResponseEntity<Erro>(new Erro(ex.getMessage()), NOT_FOUND);
    }
	
	@ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ResponseEntity<Erro> processValidationError(DataIntegrityViolationException ex) {
        return new ResponseEntity<Erro>(new Erro(ex.getMessage()), CONFLICT);
    }

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Erro> erro(Exception e) {
		log.error("Erro inesperado.", e);
		return new ResponseEntity<Erro>(new Erro("Erro interno do Sistema, favor contactar a equipe técnica"), INTERNAL_SERVER_ERROR);
	}

    private List<Erro> processFieldErrors(List<FieldError> fieldErrors) {
    	
    	List<Erro> erros = new ArrayList<>();

        for (FieldError fieldError: fieldErrors) {
            String localizedErrorMessage = resolveLocalizedErrorMessage(fieldError);
            erros.add(new Erro(fieldError.getField(), localizedErrorMessage));            
        }

        return erros;
    }

    private String resolveLocalizedErrorMessage(FieldError fieldError) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        String localizedErrorMessage = messageSource.getMessage(fieldError, currentLocale);

        return localizedErrorMessage;
    }
}
