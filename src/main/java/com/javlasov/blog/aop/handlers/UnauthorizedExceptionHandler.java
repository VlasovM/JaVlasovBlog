package com.javlasov.blog.aop.handlers;

import com.javlasov.blog.aop.exceptions.UnauthorizedExceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UnauthorizedExceptionHandler {

    @ExceptionHandler(UnauthorizedExceptions.class)
    public ResponseEntity<?> unauthorizedExceptions() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
