package com.javlasov.blog.service;

import com.javlasov.blog.api.response.LoginResponse;
import com.javlasov.blog.dto.UserDto;
import com.javlasov.blog.mappers.DtoMapper;
import com.javlasov.blog.model.User;
import com.javlasov.blog.repository.PostRepository;
import com.javlasov.blog.repository.UserRepository;
import com.javlasov.blog.security.UserDetailsServiceImp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class LoginServiceTest {

    UserRepository mockUserRepo = Mockito.mock(UserRepository.class);
    PostRepository mockPostRepo = Mockito.mock(PostRepository.class);
    UserDetailsServiceImp mockUserDetailsService = Mockito.mock(UserDetailsServiceImp.class);
    AuthenticationManager mockAuthManager = Mockito.mock(AuthenticationManager.class);
    DtoMapper mockDtoMapper = Mockito.mock(DtoMapper.class);
    LoginService underTestService = new LoginService(mockUserRepo, mockDtoMapper,
            mockPostRepo, mockUserDetailsService, mockAuthManager);

    @Test
    @DisplayName("The user is auth")
    void checkUserAuthTest() {
        LoginResponse expected = new LoginResponse();
        User user = getUser();
        UserDto userDto = getUserDto(user.getEmail(), user.getName());
        expected.setUser(userDto);
        expected.setResult(true);

        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(mockDtoMapper.userToUserDTO(user)).thenReturn(userDto);

        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn("memaks@mail.ru");

        LoginResponse actual = underTestService.checkUser(principal);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("The user not auth")
    void checkUserNotAuthTest() {
        LoginResponse expected = new LoginResponse();
        LoginResponse actual = underTestService.checkUser(null);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Logout user")
    void logoutUserCurrentSessionShouldBeNullTest() {
        LoginResponse expected = new LoginResponse();
        expected.setResult(true);

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("memaks@mail.ru");

        LoginResponse actual = underTestService.logout();


        assertEquals(expected, actual);
        assertThrows(NullPointerException.class, () -> SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Test
    @DisplayName("Login existing user in DB")
    void loginExistingUserTest() {
        LoginResponse expected = new LoginResponse();
        User user = getUser();
        String password = "12345Maks";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        user.setPassword(passwordEncoder.encode(password));
        UserDto userDto = getUserDto(user.getEmail(), user.getName());

        expected.setResult(true);
        expected.setUser(userDto);

        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(mockDtoMapper.userToUserDTO(user)).thenReturn(userDto);

        LoginResponse actual = underTestService.login(user.getEmail(), password);
        assertEquals(expected, actual);

    }

    @Test
    @DisplayName("Login not existing user")
    void loginNotExistingUserTest() {
        LoginResponse expected = new LoginResponse();

        LoginResponse actual = underTestService.login("NoExistingUser@mail.ru", "qwerty12345");

        assertEquals(expected, actual);
    }

    private User getUser() {
        User user = new User();
        user.setName("Maxim");
        user.setEmail("test@mail.ru");
        user.setModerator(0);
        return user;
    }

    private UserDto getUserDto(String email, String name) {
        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        userDto.setName(name);
        userDto.setModeration(false);
        return userDto;
    }

}