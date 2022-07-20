package com.javlasov.blog.aop.handlers;

import org.apache.tomcat.util.http.fileupload.impl.SizeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class SizeLimitExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(SizeLimitExceptionHandler.class);

    @ExceptionHandler(SizeException.class)
    protected ResponseEntity<?> handleConflict() {
        Map<String, String> message = new HashMap<>();
        message.put("message", "Превышен допустимый размер файла. Максимальынй размер файла не более " +
                "256KB.");
        logger.error("Ошибка при попатке загрузки изображения. Превышен допустимый размер файла.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

}
