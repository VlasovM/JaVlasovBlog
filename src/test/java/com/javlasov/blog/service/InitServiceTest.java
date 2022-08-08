package com.javlasov.blog.service;

import com.javlasov.blog.api.response.InitResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class InitServiceTest {

    InitService underTestService = new InitService();

    @Test
    @DisplayName("Test init service.")
    void initTest() {
        InitResponse expectedResponse = new InitResponse();

        expectedResponse.setTitle("JaVlasov");
        expectedResponse.setSubtitle("Блог о программировании и технологиях");
        expectedResponse.setEmail("JaVlasovM@gmail.com");
        expectedResponse.setCopyright("Vlasov Maxim");
        expectedResponse.setCopyrightFrom("2021");

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("test@mail.ru");

        InitResponse actualResponse = underTestService.init();

        assertEquals(expectedResponse, actualResponse);

    }
}