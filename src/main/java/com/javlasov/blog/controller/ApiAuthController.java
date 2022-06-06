package com.javlasov.blog.controller;

import com.javlasov.blog.api.request.LoginRequest;
import com.javlasov.blog.api.request.RegisterRequest;
import com.javlasov.blog.api.response.CaptchaResponse;
import com.javlasov.blog.api.response.LoginResponse;
import com.javlasov.blog.api.response.StatusResponse;
import com.javlasov.blog.service.CaptchaService;
import com.javlasov.blog.service.LoginService;
import com.javlasov.blog.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ApiAuthController {

    private final CaptchaService captchaService;

    private final RegisterService registerService;

    private final LoginService loginService;

    @GetMapping("/check")
    public ResponseEntity<LoginResponse> check(Principal principal) {
        return ResponseEntity.ok(loginService.checkUser(principal));
    }

    @GetMapping("/captcha")
    public ResponseEntity<CaptchaResponse> captcha() {
        return ResponseEntity.ok(captchaService.getCaptcha());
    }

    @PostMapping("/register")
    public ResponseEntity<StatusResponse> register(@Valid @RequestBody RegisterRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.ok(registerService.getRegisterWithErrors(bindingResult.getAllErrors()));
        }
        return ResponseEntity.ok(registerService.register(request.getEmail(), request.getPassword(), request.getName(),
                request.getCaptcha(), request.getCaptchaSecret()));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(loginService.login(request.getEmail(), request.getPassword()));
    }

    @GetMapping("/logout")
    public ResponseEntity<LoginResponse> logout() {
        return ResponseEntity.ok(loginService.logout());
    }

}
