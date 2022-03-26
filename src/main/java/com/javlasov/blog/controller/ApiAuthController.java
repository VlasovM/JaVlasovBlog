package com.javlasov.blog.controller;

import com.javlasov.blog.api.response.CheckResponse;
import com.javlasov.blog.service.CheckService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final CheckService checkService;

    public ApiAuthController(CheckService checkService) {
        this.checkService = checkService;
    }

    @GetMapping("/check")
    public CheckResponse check() {
        return checkService.checkUser();
    }
}
