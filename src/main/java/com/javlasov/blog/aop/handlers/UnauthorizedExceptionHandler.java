package com.javlasov.blog.aop.handlers;

import com.javlasov.blog.aop.exceptions.UnauthorizedExceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class UnauthorizedExceptionHandler {

    @ExceptionHandler(UnauthorizedExceptions.class)
    public ResponseEntity<Map<String, String>> unauthorizedExceptions() {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Ошибка доступа. Вам необходимо авторизироваться");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errors);
    }

}
