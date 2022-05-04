package com.javlasov.blog.service;

import com.javlasov.blog.api.response.RegisterResponse;
import com.javlasov.blog.model.User;
import com.javlasov.blog.repository.CaptchaRepository;
import com.javlasov.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final UserRepository userRepository;
    private final CaptchaRepository captchaRepository;

    public RegisterResponse register(String email, String password, String name,
                                     String captcha, String secret) {
        RegisterResponse registerResponse = new RegisterResponse();
        Map<String, String> errors = checkInputData(email, captcha, secret);
        if (errors.isEmpty()) {
            registerResponse.setResult(true);
            registerResponse.setErrors(null);
            addUserInDB(email, password, name);
            return registerResponse;
        }
        registerResponse.setErrors(errors);
        registerResponse.setResult(false);
        return registerResponse;
    }

    public RegisterResponse getRegisterWithErrors(List<ObjectError> listErrors) {
        RegisterResponse response = new RegisterResponse();
        Map<String, String> errors = new HashMap<>();
        listErrors.forEach((error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        }));
        response.setResult(false);
        response.setErrors(errors);
        return response;
    }

    private Map<String, String> checkInputData(String email, String captcha, String secret) {
        Map<String, String> result = new TreeMap<>();
        checkEmail(email, result);
        checkCaptcha(captcha, secret, result);
        return result;
    }

    private void checkEmail(String email, Map<String, String> errors) {
        List<User> usersList = userRepository.findAll();
        for (User user : usersList) {
            boolean isContains = Pattern.compile(Pattern.quote(email), Pattern.CASE_INSENSITIVE)
                    .matcher(user.getEmail()).find();
            if (isContains) {
                errors.put("email", "Такая почта уже зарегистрирована");
            }
        }
    }

    private void checkCaptcha(String captchaUser, String secret, Map<String, String> errors) {
        if (captchaRepository.existsBySecretCode(secret)) {
            String code = captchaRepository.findBySecretCode(secret).get().getCode();
            if (!code.equals(captchaUser)) {
                errors.put("captcha", "Код с картинки введён неверно");
            }
            return;
        }
        errors.put("captcha", "Код с картинки введён неверно");
    }

    private void addUserInDB(String email, String password, String name) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(passwordEncoder.encode(password));
        user.setModerator(0);
        user.setRegTime(LocalDateTime.now());
        userRepository.save(user);
    }
}
