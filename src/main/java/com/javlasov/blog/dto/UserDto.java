package com.javlasov.blog.dto;

import lombok.Data;

@Data
public class UserDto {

    private int id;

    private String name;

    private String photo;

    private String email;

    private boolean moderation;

    private int moderationCount;

    private boolean settings;

}
