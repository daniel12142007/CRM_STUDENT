package com.kaitech.student_crm.exceptions;

import com.kaitech.student_crm.payload.response.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<String> handleInvalidEmailException(InvalidEmailException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<String> handleUserExistException(UserExistException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<String> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {
        return new ResponseEntity<>(ex.getReason(), ex.getStatus());
    }

    @ExceptionHandler(ProjectAlreadyCompletedException.class)
    public ResponseEntity<String> handleProjectAlreadyCompletedException(ProjectAlreadyCompletedException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(LevelBadRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<MessageResponse> handleLevelBadRequest(LevelBadRequest e) {
        return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<MessageResponse> handleLevelNotFound(RuntimeException e) {
        return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}