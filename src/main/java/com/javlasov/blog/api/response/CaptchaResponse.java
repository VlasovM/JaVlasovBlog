package com.javlasov.blog.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CaptchaResponse {

    @JsonProperty("secret")
    private String secretCode;
    @JsonProperty("image")
    private String imageBase64;

}
