package com.gl.ceir.panel.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gl.ceir.panel.dto.response.ErrorDto;
import com.gl.ceir.panel.dto.response.MessageResponse;
import com.gl.ceir.panel.exception.UserNotFoundException;

import lombok.extern.log4j.Log4j2;

@SuppressWarnings("unused")
@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
		Map<String, MessageResponse> map = new HashMap<>();
		MessageResponse response = MessageResponse.builder().code(HttpStatus.BAD_REQUEST).status("fail").build();
		ex.getBindingResult().getFieldErrors().forEach(e -> {
			if(map.containsKey(e.getField())) {
				response.getMessages().add(ErrorDto.builder().field(e.getField()).message(map.get(e.getField()) + "," + e.getDefaultMessage()).build());
			} else {
				response.getMessages().add(ErrorDto.builder().field(e.getField()).message(e.getDefaultMessage()).build());
			}
		});
		
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, List<String>>> handleNotFoundException(UserNotFoundException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        ex.printStackTrace();
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Map<String, List<String>>> handleGeneralExceptions(Exception ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        ex.printStackTrace();
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<Map<String, List<String>>> handleRuntimeExceptions(RuntimeException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        ex.printStackTrace();
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }
}
