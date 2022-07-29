package com.javlasov.blog.service;

import com.javlasov.blog.api.response.StatusResponse;
import com.javlasov.blog.model.CaptchaCodes;
import com.javlasov.blog.model.User;
import com.javlasov.blog.repository.CaptchaRepository;
import com.javlasov.blog.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RegisterServiceTest {

    UserRepository mockUserRepo = Mockito.mock(UserRepository.class);
    CaptchaRepository mockCaptchaRepo = Mockito.mock(CaptchaRepository.class);
    RegisterService underTestService = new RegisterService(mockUserRepo, mockCaptchaRepo);

    @Test
    @DisplayName("Register user without errors")
    void registerUserWithoutErrorsTest() {
        StatusResponse expectedResponse = new StatusResponse();
        expectedResponse.setResult(true);
        User user = getUser();

        CaptchaCodes captchaCodes = getCaptcha();

        when(mockCaptchaRepo.findBySecretCode(captchaCodes.getSecretCode())).thenReturn(Optional.of(captchaCodes));
        when(mockCaptchaRepo.existsBySecretCode(captchaCodes.getSecretCode())).thenReturn(true);

        StatusResponse actualResponse = underTestService.register(
                user.getEmail(),
                user.getPassword(),
                user.getName(),
                captchaCodes.getCode(),
                captchaCodes.getSecretCode());

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Register user with email error")
    void registerUserWithEmailErrorTest() {
        StatusResponse expectedResponse = new StatusResponse();
        Map<String, String> errorsMap = new HashMap<>();
        errorsMap.put("email", "Такая почта уже зарегистрирована");
        expectedResponse.setErrors(errorsMap);

        User user = getUser();
        List<User> usersList = new ArrayList<>();
        usersList.add(user);

        CaptchaCodes captchaCodes = getCaptcha();

        when(mockUserRepo.findAll()).thenReturn(usersList);
        when(mockCaptchaRepo.existsBySecretCode(captchaCodes.getSecretCode())).thenReturn(true);
        when(mockCaptchaRepo.findBySecretCode(captchaCodes.getSecretCode())).thenReturn(Optional.of(captchaCodes));

        StatusResponse actualResponse = underTestService.register(
                user.getEmail(),
                user.getPassword(),
                user.getName(),
                captchaCodes.getCode(),
                captchaCodes.getSecretCode());

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Register user with captcha error")
    void registerUserWithCaptchaError() {
        StatusResponse expectedResponse = new StatusResponse();
        Map<String, String> errorsMap = new HashMap<>();
        errorsMap.put("captcha", "Код с картинки введён неверно");
        expectedResponse.setErrors(errorsMap);

        User user = getUser();
        CaptchaCodes captcha = getCaptcha();

        List<User> usersList = new ArrayList<>();

        when(mockUserRepo.findAll()).thenReturn(usersList);
        when(mockCaptchaRepo.existsBySecretCode(captcha.getSecretCode())).thenReturn(true);
        when(mockCaptchaRepo.findBySecretCode(captcha.getSecretCode())).thenReturn(Optional.of(captcha));

        StatusResponse actualResponse = underTestService.register(
                user.getEmail(),
                user.getPassword(),
                user.getName(),
                "Incorrect code",
                captcha.getSecretCode()
        );

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Test method register with empty error list.")
    void registerWithErrorsTest() {
        StatusResponse expectedResponse = new StatusResponse();
        expectedResponse.setErrors(new HashMap<>());

        ObjectError error = new ObjectError("someName", "someMessage");
        List<ObjectError> errorsList = new ArrayList<>() {};
        errorsList.add(error);

        StatusResponse actualResponse = underTestService.getRegisterWithErrors(errorsList);

        assertEquals(expectedResponse, actualResponse);
    }

    private User getUser() {
        User user = new User();
        user.setName("Maxim");
        user.setEmail("test@mail.ru");
        user.setModerator(0);
        user.setPassword("qwerty12345");
        return user;
    }

    private CaptchaCodes getCaptcha() {
        CaptchaCodes captchaCodes = new CaptchaCodes();
        captchaCodes.setCode("qahusukimo");
        captchaCodes.setSecretCode("f58026d4-50b8-4125-b075-ce8f60069b37");
        captchaCodes.setTime(LocalDateTime.now());
        return captchaCodes;
    }

}