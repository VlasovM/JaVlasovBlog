package com.javlasov.blog.service;

import com.javlasov.blog.annotations.Name;
import com.javlasov.blog.api.response.StatusResponse;
import com.javlasov.blog.model.User;
import com.javlasov.blog.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ProfileServiceTest {

    UserRepository mockUserRepo = Mockito.mock(UserRepository.class);
    ProfileService underTestService = new ProfileService(mockUserRepo);
    //Change these variables to their own values
    private final String PATH_TO_FILE_JPG = "C:\\Users\\79153\\Desktop\\Other\\5.jpg";
    private final String PATH_TO_FILE_PDF = "C:\\Users\\79153\\Desktop\\Other\\bill.pdf";


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
    @DisplayName("Attempt to set existing email. Method without photo.")
    void attemptToSetExistingEmailTest() {
        StatusResponse expectedResponse = new StatusResponse();
        Map<String, String> errors = new HashMap<>();
        errors.put("email", "Этот e-mail уже зарегистрирован");
        expectedResponse.setErrors(errors);

        User user = getUser();

        User anotherUser = new User();
        anotherUser.setEmail("another@mail.ru");
        anotherUser.setPassword("another12345");
        anotherUser.setModerator(0);

        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(mockUserRepo.findByEmail(anotherUser.getEmail())).thenReturn(Optional.of(anotherUser));


        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getEmail());

        StatusResponse actualResponse = underTestService.editMyProfileWithoutPhoto(
                user.getName(), anotherUser.getEmail(), user.getPassword(), 0);

        assertEquals(expectedResponse, actualResponse);

    }


    @Test
    @DisplayName("Upload photo correct format. Method with photo.")
    void uploadPhotoTest() throws IOException {
        StatusResponse expectedResponse = new StatusResponse();
        expectedResponse.setResult(true);

        User user = getUser();
        String expectedPassword = user.getPassword();
        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));


        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getEmail());

        FileInputStream fis = new FileInputStream(PATH_TO_FILE_JPG);
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "Photo", "5.jpg", "image/jpeg", fis);

        StatusResponse actualResponse = underTestService.editMyProfileWithPhoto(
                mockMultipartFile, user.getName(), user.getEmail(), user.getPassword());


        assertEquals(expectedResponse, actualResponse);
        assertEquals(expectedPassword, user.getPassword());

        fis.close();
    }

    @Test
    @DisplayName("Attempt to upload pdf file. Method with photo.")
    void uploadInCorrectFormatPhotoTest() throws IOException {
        StatusResponse expectedResponse = new StatusResponse();
        expectedResponse.setResult(false);
        Map<String, String> errors = new HashMap<>();
        errors.put("photo", "Некорректный формат файла. Допустимые форматы: png, jpg(jpeg)");
        expectedResponse.setErrors(errors);

        User user = getUser();
        String expectedPassword = user.getPassword();

        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getEmail());

        FileInputStream fis = new FileInputStream(PATH_TO_FILE_PDF);
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "JpgFile", "bill", "application/pdf", fis);

        StatusResponse actualResponse = underTestService.editMyProfileWithPhoto(
                mockMultipartFile, user.getName(), user.getEmail(), user.getPassword());


        assertEquals(expectedResponse, actualResponse);
        assertEquals(expectedPassword, user.getPassword());

        fis.close();
    }

    @Test
    @DisplayName("Upload correct photo and change pass.")
    void uploadPhotoAndChangePass() throws IOException {
        StatusResponse expectedResponse = new StatusResponse();
        expectedResponse.setResult(true);

        User user = getUser();
        String oldPassword = user.getPassword();
        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));


        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getEmail());

        FileInputStream fis = new FileInputStream(PATH_TO_FILE_JPG);
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "Photo", "5.jpg", "image/jpeg", fis);

        StatusResponse actualResponse = underTestService.editMyProfileWithPhoto(
                mockMultipartFile, user.getName(), user.getEmail(), "newPassword12345");


        assertEquals(expectedResponse, actualResponse);
        assertNotEquals(oldPassword, user.getPassword());

        fis.close();
    }

    @Test
    @DisplayName("Attempt to set existing email. Method with photo.")
    void attemptToSetExistingEmailWithPhotoTest() throws IOException {
        StatusResponse expectedResponse = new StatusResponse();
        Map<String, String> errors = new HashMap<>();
        errors.put("email", "Этот e-mail уже зарегистрирован");
        expectedResponse.setErrors(errors);

        User user = getUser();

        User anotherUser = new User();
        anotherUser.setEmail("another@mail.ru");
        anotherUser.setPassword("another12345");
        anotherUser.setModerator(0);

        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(mockUserRepo.findByEmail(anotherUser.getEmail())).thenReturn(Optional.of(anotherUser));

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getEmail());

        FileInputStream fis = new FileInputStream(PATH_TO_FILE_JPG);
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "Photo", "5.jpg", "image/jpeg", fis);

        StatusResponse actualResponse = underTestService.editMyProfileWithPhoto(
                mockMultipartFile, user.getName(), anotherUser.getEmail(), user.getPassword());

        assertEquals(expectedResponse, actualResponse);

        fis.close();
    }

    @Test
    @DisplayName("Test errors method with name and email error.")
    void getRegisterWithErrorsTest() {
        StatusResponse expectedResponse = new StatusResponse();
        Map<String, String> errorsMapExpected = new HashMap<>();

        errorsMapExpected.put("name", "Имя должно содержать только буквы и состоять минимум из 2 символов");
        errorsMapExpected.put("email", "Неверный формат введённого e-mail");
        expectedResponse.setErrors(errorsMapExpected);

        List<ObjectError> errorsList = new ArrayList<>();

        ObjectError errorName = new FieldError("FieldError", "name",
                "Имя должно содержать только буквы и состоять минимум из 2 символов");
        ObjectError errorEmail = new FieldError("FieldError", "email",
                "Неверный формат введённого e-mail");

        errorsList.add(errorName);
        errorsList.add(errorEmail);

        StatusResponse actualResponse = underTestService.getRegisterWithErrors(errorsList);
        assertEquals(expectedResponse, actualResponse);
    }

    private User getUser() {
        User user = new User();
        user.setName("Maxim");
        user.setEmail("test@mail.ru");
        user.setModerator(0);
        return user;
    }

}