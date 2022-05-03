package com.javlasov.blog.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Permission {

    USER("user:write"),
    MODERATE("user:moderate");

    private final String permission;

}
