package com.api.note.config.system;

import com.api.note.exceptions.DuplicatedException;
import com.api.note.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {

    @ExceptionHandler(DuplicatedException.class)
    public ResponseEntity<Object> handleDuplicatedException(DuplicatedException ex) {
        String errorMessage = ex.getMessage();
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        return new ResponseEntity<>(errorMessage,httpStatus);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleDNotFoundException(NotFoundException ex) {
        String errorMessage = ex.getMessage();
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(errorMessage,httpStatus);
    }
}
