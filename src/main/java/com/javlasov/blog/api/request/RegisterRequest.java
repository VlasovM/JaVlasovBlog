package com.javlasov.blog.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegisterRequest {

    private String name;

    private String password;

    @JsonProperty(value = "e_mail")
    private String email;

    private String captcha;

    @JsonProperty("captcha_secret")
    private String captchaSecret;

}
