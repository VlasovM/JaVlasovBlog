package com.javlasov.blog.service;

import com.javlasov.blog.api.response.StatusResponse;
import com.javlasov.blog.model.User;
import com.javlasov.blog.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ProfileServiceTest {

    UserRepository mockUserRepo = Mockito.mock(UserRepository.class);
    ProfileService underTestService = new ProfileService(mockUserRepo);


    @Test
    @DisplayName("Nothing change. Name, email and pass must be unchanged. Method without photo.")
    void nothingChangeTest() {
        StatusResponse expectedResponse = new StatusResponse();
        expectedResponse.setResult(true);

        User user = getUser();

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getEmail());

        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        String expectedUserName = user.getName();
        String expectedPassword = user.getPassword();
        String expectedEmail = user.getEmail();
        StatusResponse actualResponse = underTestService.editMyProfileWithoutPhoto(
                user.getName(), user.getEmail(), null, 0);

        assertEquals(expectedResponse, actualResponse);
        assertEquals(expectedUserName, user.getName());
        assertEquals(expectedPassword, user.getPassword());
        assertEquals(expectedEmail, user.getEmail());

    }

    @Test
    @DisplayName("Change username and email. Method without photo.")
    void changeUsernameAndEmailTest() {
        StatusResponse expectedResponse = new StatusResponse();
        expectedResponse.setResult(true);

        User user = getUser();

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getEmail());

        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        String expectedUsername = "Robert";
        String expectedEmail = "test@mail.ru";

        StatusResponse actualResponse = underTestService.editMyProfileWithoutPhoto(
                expectedUsername, expectedEmail, null, 0);

        assertEquals(expectedResponse, actualResponse);
        assertEquals(expectedUsername, user.getName());
        assertEquals(expectedEmail, user.getEmail());

    }

    @Test
    @DisplayName("Change password. Method without photo.")
    void changePasswordTest() {
        StatusResponse expectedResponse = new StatusResponse();
        expectedResponse.setResult(true);

        User user = getUser();

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getEmail());

        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        String oldPassword = user.getPassword();

        StatusResponse actualResponse = underTestService.editMyProfileWithoutPhoto(
                user.getName(), user.getEmail(), "qwerty54321", 0);

        assertEquals(expectedResponse, actualResponse);
        assertNotEquals(oldPassword, user.getPassword());

    }

    @Test
    @DisplayName("Only remove photo. Method without photo.")
    void removePhotoTest() {
        StatusResponse expectedResponse = new StatusResponse();
        expectedResponse.setResult(true);

        User user = getUser();
        user.setPhoto("/test/photo/somePath");

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getEmail());

        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        StatusResponse actualResponse = underTestService.editMyProfileWithoutPhoto(
                user.getName(), user.getEmail(), "qwerty54321", 1);

        assertEquals(expectedResponse, actualResponse);
        assertNull(user.getPhoto());

    }



    @Test
    void editMyProfileWithPhoto() {
    }

    @Test
    void getRegisterWithErrors() {
    }

    private User getUser() {
        User user = new User();
        user.setName("Maxim");
        user.setEmail("test@mail.ru");
        user.setModerator(0);
        return user;
    }

}