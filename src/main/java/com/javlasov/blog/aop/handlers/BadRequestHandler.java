package com.javlasov.blog.aop.handlers;

import com.javlasov.blog.aop.exceptions.BadRequestExceptions;
import com.javlasov.blog.api.response.StatusResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BadRequestHandler {

    @ExceptionHandler(BadRequestExceptions.class)
    private ResponseEntity<StatusResponse> handleConflict(BadRequestExceptions exceptions) {
        StatusResponse statusResponse = new StatusResponse();
        statusResponse.setErrors(exceptions.getErrors());
        return ResponseEntity.badRequest().body(statusResponse);

    }

}
