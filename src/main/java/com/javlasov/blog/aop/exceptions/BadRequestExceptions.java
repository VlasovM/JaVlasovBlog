package com.javlasov.blog.aop.exceptions;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@RequiredArgsConstructor
@Data
public class BadRequestExceptions extends RuntimeException {

    private Map<String, String> errors;

    public BadRequestExceptions(Map<String, String> errors) {
        this.errors = errors;
    }
}

