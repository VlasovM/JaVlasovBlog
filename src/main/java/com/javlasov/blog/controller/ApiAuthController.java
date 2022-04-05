package com.javlasov.blog.controller;

import com.javlasov.blog.service.CheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ApiAuthController {

    private final CheckService checkService;

    @GetMapping("/check")
    public ResponseEntity<CheckService> check() {
        return new ResponseEntity(checkService.checkUser(), HttpStatus.OK);
    }
}
