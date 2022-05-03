package com;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


public class TestBcryptEncoder {

    @Test
    public void testBcrypt() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

        String password = "qwerty";

        for (int i = 0; i < 5; i++) {
            String encodePassword = passwordEncoder.encode(password);
            System.out.println(encodePassword);
            System.out.println(passwordEncoder.matches(password, encodePassword));
        }
    }
}
