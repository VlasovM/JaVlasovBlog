package com.javlasov.blog.controller;

import com.javlasov.blog.api.request.LoginRequest;
import com.javlasov.blog.api.request.PasswordRequest;
import com.javlasov.blog.api.request.RegisterRequest;
import com.javlasov.blog.api.request.RestoreRequest;
import com.javlasov.blog.api.response.CaptchaResponse;
import com.javlasov.blog.api.response.LoginResponse;
import com.javlasov.blog.api.response.StatusResponse;
import com.javlasov.blog.model.GlobalSettings;
import com.javlasov.blog.repository.GlobalSettingRepository;
import com.javlasov.blog.service.CaptchaService;
import com.javlasov.blog.service.LoginService;
import com.javlasov.blog.service.PasswordService;
import com.javlasov.blog.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    private final PasswordService passwordService;

    private final GlobalSettingRepository globalSettingRepository;

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
        GlobalSettings multiuserMode = globalSettingRepository.findById(1);
        if (multiuserMode.getValue().equals("NO")) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<LoginResponse> logout() {
        return ResponseEntity.ok(loginService.logout());
    }

    @PostMapping("/restore")
    public ResponseEntity<StatusResponse> restorePassword(@RequestBody RestoreRequest restoreRequest) {
        return ResponseEntity.ok(passwordService.restorePassword(restoreRequest.getEmail()));
    }

    @PostMapping("/password")
    public ResponseEntity<StatusResponse> changePassword(@RequestBody PasswordRequest passwordRequest) {
        return ResponseEntity.ok(passwordService.changePassword(passwordRequest.getCode(), passwordRequest.getPassword(),
                passwordRequest.getCaptcha(), passwordRequest.getCaptchaSecret()));
    }

}
