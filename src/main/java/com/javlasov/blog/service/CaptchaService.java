package com.javlasov.blog.service;

import com.github.cage.GCage;
import com.javlasov.blog.api.response.CaptchaResponse;
import com.javlasov.blog.model.CaptchaCodes;
import com.javlasov.blog.repository.CaptchaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CaptchaService {

    private final CaptchaRepository captchaRepository;

    public CaptchaResponse getCaptcha() {
        deleteOldCaptchaCodes();
        CaptchaResponse captchaResponse = new CaptchaResponse();
        GCage captcha = new GCage();
        String codeCaptcha = captcha.getTokenGenerator().next();
        String image = getImageBase64(captcha, codeCaptcha);
        String secretCode = generateSecretCode();
        captchaResponse.setImageBase64(image);
        captchaResponse.setSecretCode(secretCode);
        saveCaptchaToDb(codeCaptcha, secretCode);
        return captchaResponse;
    }

    private String getImageBase64(GCage gCage, String code) {
        byte[] fileContent = gCage.draw(code);
        String encodedString = Base64.getEncoder().encodeToString(fileContent);
        return "data:image/png;base64, " + encodedString;
    }

    private String generateSecretCode() {
        return UUID.randomUUID().toString();
    }


    private void saveCaptchaToDb(String codeCaptcha, String secretCode) {
        CaptchaCodes captcha = new CaptchaCodes();
        captcha.setSecretCode(secretCode);
        captcha.setCode(codeCaptcha);
        captcha.setTime(LocalDateTime.now());
        captchaRepository.save(captcha);
    }

    private void deleteOldCaptchaCodes() {
        List<CaptchaCodes> allCaptcha = captchaRepository.findAll();
        LocalDateTime boundaryTime = LocalDateTime.now().minusHours(1);
        for (CaptchaCodes captcha : allCaptcha) {
            if (captcha.getTime().isBefore(boundaryTime)) {
                captchaRepository.delete(captcha);
            }
        }
    }

}
