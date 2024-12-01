package com.weather_information_api.exception;

import java.util.Date;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.weather_information_api.entity.ErrorObject;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	// Bad request exception
		@ExceptionHandler(MethodArgumentTypeMismatchException.class)
		public ResponseEntity<ErrorObject> handleMethodArgumentMismatchException(MethodArgumentTypeMismatchException ex,
				WebRequest request) {
			ErrorObject errorObject = new ErrorObject();
			errorObject.setStatusCode(HttpStatus.BAD_REQUEST.value());
			errorObject.setMessage(ex.getMessage());
			errorObject.setTimestamp(new Date());

			return new ResponseEntity<>(errorObject, HttpStatus.BAD_REQUEST);
		}
		
		// Generalized exception
		@ExceptionHandler(Exception.class)
		public ResponseEntity<ErrorObject> handleGeneralException(Exception ex, WebRequest request) {
			ErrorObject errorObject = new ErrorObject();
			errorObject.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			errorObject.setMessage(ex.getMessage());
			errorObject.setTimestamp(new Date());

			return new ResponseEntity<>(errorObject, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// Handle validation exceptions
		@ExceptionHandler(ConstraintViolationException.class)
		public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
			Map<String, String> errors = new HashMap<>();

			ex.getConstraintViolations()
					.forEach(violation -> errors.put(violation.getPropertyPath().toString(), violation.getMessage()));

			return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
		}
		
		@ExceptionHandler(ResourceNotFoundException.class)
		public ResponseEntity<ErrorObject> handleResourceNotFoundException(ResourceNotFoundException ex,
				WebRequest request) {
			ErrorObject errorObject = new ErrorObject();
			errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
			errorObject.setMessage(ex.getMessage());
			errorObject.setTimestamp(new Date());

			return new ResponseEntity<>(errorObject, HttpStatus.NOT_FOUND);
		}
		
    
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		Map<String, Object> body = new HashMap<>();

		body.put("statusCode", HttpStatus.BAD_REQUEST.value());
		body.put("timestamp", new Date());
		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x -> x.getDefaultMessage())
				.collect(Collectors.toList());

		body.put("messages", errors);
		logger.warn("Validation failed: {}", errors);
		

		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
}
