package com.javlasov.blog.api.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.javlasov.blog.annotations.Email;
import com.javlasov.blog.annotations.Name;
import com.javlasov.blog.annotations.Password;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EditProfileRequest {

    @Name
    String name;
    @Email
    String email;
    @Password
    String password;
    
    int removePhoto;

}
