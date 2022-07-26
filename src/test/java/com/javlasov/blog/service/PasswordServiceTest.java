package com.javlasov.blog.service;

import com.javlasov.blog.api.response.StatusResponse;
import com.javlasov.blog.model.CaptchaCodes;
import com.javlasov.blog.model.User;
import com.javlasov.blog.repository.CaptchaRepository;
import com.javlasov.blog.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PasswordServiceTest {

    UserRepository mockUserRepo = Mockito.mock(UserRepository.class);
    CaptchaRepository mockCaptchaRepo = Mockito.mock(CaptchaRepository.class);
    PasswordService underTestService = new PasswordService(mockUserRepo, mockCaptchaRepo);


    @Test
    @DisplayName("Restore password from existing User")
    void restorePasswordFromExistingUserTest() {
        StatusResponse expected = new StatusResponse();
        expected.setResult(true);
        User user = getUser();

        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        StatusResponse actual = underTestService.restorePassword(user.getEmail());

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Chane password without errors")
    void changePasswordWithoutErrorTest() {
        StatusResponse expected = new StatusResponse();
        expected.setResult(true);

        User user = getUser();
        CaptchaCodes captcha = getCaptcha();
        String hashCode = UUID.randomUUID().toString().replaceAll("-", "");
        user.setCode(hashCode);

        when(mockUserRepo.findByCode(hashCode)).thenReturn(Optional.of(user));
        when(mockCaptchaRepo.findBySecretCode(captcha.getSecretCode())).thenReturn(Optional.of(captcha));

        StatusResponse actual = underTestService.changePassword(hashCode, "123456qwerty", captcha.getCode(),
                captcha.getSecretCode());

        assertEquals(expected, actual);

    }

    @Test
    @DisplayName("Chane password with error")
    void changePasswordWithErrorTest() {
        StatusResponse expected = new StatusResponse();
        expected.setResult(false);
        Map<String, String> errorsExpected = new HashMap<>();
        errorsExpected.put("captcha", "Код с картинки введен неверно.");
        expected.setErrors(errorsExpected);

        User user = getUser();
        CaptchaCodes captcha = getCaptcha();
        String hashCode = UUID.randomUUID().toString().replaceAll("-", "");
        user.setCode(hashCode);

        when(mockUserRepo.findByCode(hashCode)).thenReturn(Optional.of(user));
        when(mockCaptchaRepo.findBySecretCode(captcha.getSecretCode())).thenReturn(Optional.of(captcha));

        StatusResponse actual = underTestService.changePassword(hashCode, "123456qwerty",
                "IncorrectCaptchaCode", captcha.getSecretCode());

        assertEquals(expected, actual);

    }

    private User getUser() {
        User user = new User();
        user.setName("Maxim");
        user.setEmail("test@mail.ru");
        user.setModerator(0);
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