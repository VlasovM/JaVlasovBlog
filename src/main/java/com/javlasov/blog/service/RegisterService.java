package com.javlasov.blog.service;

import com.javlasov.blog.api.response.RegisterResponse;
import com.javlasov.blog.entity.CaptchaCodes;
import com.javlasov.blog.entity.User;
import com.javlasov.blog.repository.CaptchaRepository;
import com.javlasov.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
                                     String captchaUser, String captchaBase64) {
        RegisterResponse registerResponse = new RegisterResponse();
        Map<String, String> errors = checkInputData(email, name, password, captchaUser, captchaBase64);
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

    private Map<String, String> checkInputData(String email, String name, String pass,
                                               String captchaUser, String captchaBase64) {
        Map<String, String> result = new TreeMap<>();

        String emailUser = checkEmail(email);
        if (!emailUser.equals(email)) {
            result.put("email", emailUser);
        }

        String nameUser = checkName(name);
        if (!nameUser.equals(name)) {
            result.put("name", nameUser);
        }

        String passUser = checkPassword(pass);
        if (!passUser.equals(pass)) {
            result.put("password", passUser);
        }

        if (!checkCaptcha(captchaUser, captchaBase64)) {
            result.put("captcha", "Код с картинки введён неверно");
        }

        return result;
    }

    private String checkEmail(String email) {
        String validateEmail = "^[A-Za-z0-9]+(.)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]{2,3})$";
        if (email.matches(validateEmail)) {
            List<User> usersList = userRepository.findAll();
            for (User user : usersList) {
                boolean isContains = Pattern.compile(Pattern.quote(email), Pattern.CASE_INSENSITIVE)
                        .matcher(user.getEmail()).find();
                if (isContains) {
                    return "Этот e-mail уже зарегистрирован";
                }
            }
            return email;
        }
        return "Неверный формат введённого e-mail.";
    }

    private String checkName(String name) {
        for (char c : name.toCharArray()) {
            if (!Character.isAlphabetic(c)) {
                return "Имя может содержать только буквы.";
            }
        }
        return name;
    }

    private String checkPassword(String pass) {
        String regExp = "[a-zA-Z0-9]{7,}";
        if (!(pass.length() <= 7)) {
            if (!pass.matches(regExp)) {
                return "Пароль должен содержать буквы и цифры и иметь буквы разного регистра";
            }
            return pass;
        }
        return "Пароль не может быть короче 7 символов.";
    }

    private boolean checkCaptcha(String captchaUser, String captchaBase64) {
        CaptchaCodes captcha = captchaRepository.findByCode(captchaBase64);
        return captcha.getSecretCode().equals(captchaUser);
    }

    private void addUserInDB(String email, String password, String name) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(password);
        user.setIsModerator(0);
        user.setRegTime(LocalDateTime.now());
    }
}
