package com.javlasov.blog.controller;

import com.javlasov.blog.api.request.RegisterRequest;
import com.javlasov.blog.api.response.CaptchaResponse;
import com.javlasov.blog.api.response.CheckResponse;
import com.javlasov.blog.api.response.RegisterResponse;
import com.javlasov.blog.service.CaptchaService;
import com.javlasov.blog.service.CheckService;
import com.javlasov.blog.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ApiAuthController {

    private final CheckService checkService;
    private final CaptchaService captchaService;
    private final RegisterService registerService;

    @GetMapping("/check")
    public ResponseEntity<CheckResponse> check() {
        return ResponseEntity.ok(checkService.checkUser());
    }

    @GetMapping("/captcha")
    public ResponseEntity<CaptchaResponse> captcha() {
        return ResponseEntity.ok(captchaService.getCaptcha());
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        System.out.println(request.getEmail());
        System.out.println(request.getPassword());
        System.out.println(request.getName());
        return ResponseEntity.ok(registerService.register(request.getEmail(), request.getPassword(), request.getName(),
                request.getCaptcha(), request.getCaptchaSecret()));
    }
}
