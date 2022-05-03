package com.javlasov.blog.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.javlasov.blog.dto.UserDto;
import lombok.Data;

@Data
public class LoginResponse {

    private boolean result;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserDto user;

}
