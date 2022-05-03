package com.javlasov.blog.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.javlasov.blog.annotations.Email;
import com.javlasov.blog.annotations.Name;
import com.javlasov.blog.annotations.Password;
import lombok.Data;

@Data
public class RegisterRequest {

    @Name
    private String name;

    @Password
    private String password;

    @JsonProperty(value = "e_mail")
    @Email
    private String email;

    private String captcha;

    @JsonProperty("captcha_secret")
    private String captchaSecret;

}
