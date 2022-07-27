package com.javlasov.blog.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.javlasov.blog.annotations.Password;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PasswordRequest {

    String code;

    @Password
    String password;

    String captcha;

    @JsonProperty("captcha_secret")
    String captchaSecret;

}
