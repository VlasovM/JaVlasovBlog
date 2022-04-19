package com.javlasov.blog.service;

import com.github.cage.GCage;
import com.javlasov.blog.api.response.CaptchaResponse;
import com.javlasov.blog.entity.CaptchaCodes;
import com.javlasov.blog.repository.CaptchaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Formatter;

@Service
@RequiredArgsConstructor
public class CaptchaService {

    private final CaptchaRepository captchaRepository;

    public CaptchaResponse getCaptcha() {
        CaptchaResponse captchaResponse = new CaptchaResponse();
        GCage captcha = new GCage();
        String secretCode = captcha.getTokenGenerator().next();
        String code = convertCodeToBase64(captcha, secretCode);
        captchaResponse.setSecretCode(secretCode);
        captchaResponse.setImageBase64(code);
        saveCaptchaInDB(captchaResponse);
    return captchaResponse;
    }

    private String convertCodeToBase64(GCage code, String secretCode) {
        byte[] fileContent = code.draw(secretCode);
        String encodedString = Base64.getEncoder().encodeToString(fileContent);
        return "data:image/png;base64, " + encodedString;
    }

    private void saveCaptchaInDB(CaptchaResponse captchaResponse) {
        CaptchaCodes captcha = new CaptchaCodes();
        captcha.setSecretCode(captchaResponse.getSecretCode());
        captcha.setCode(captchaResponse.getImageBase64());
        captcha.setTime(LocalDateTime.now());
        captchaRepository.save(captcha);
    }
}
