package com.javlasov.blog.advice;

import com.javlasov.blog.exceptions.UnauthorizedExceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UnauthorizedAdvice {

    @ExceptionHandler(UnauthorizedExceptions.class)
    public ResponseEntity<?> unauthorizedExceptions() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
