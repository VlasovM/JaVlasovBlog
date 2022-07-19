package com.javlasov.blog.aop.exceptions;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
@AllArgsConstructor
public class UnauthorizedExceptions extends RuntimeException{
}
