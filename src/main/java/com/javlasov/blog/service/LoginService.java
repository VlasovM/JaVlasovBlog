package com.javlasov.blog.service;

import com.javlasov.blog.api.response.LoginResponse;
import com.javlasov.blog.dto.UserDto;
import com.javlasov.blog.mappers.DtoMapper;
import com.javlasov.blog.model.User;
import com.javlasov.blog.repository.PostRepository;
import com.javlasov.blog.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Data
@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final DtoMapper dtoMapper;
    private final PostRepository postRepository;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    public LoginResponse checkUser(Principal principal) {
        LoginResponse loginResponse = new LoginResponse();
        if (principal == null) {
            return loginResponse;
        }
        UserDto user = prepareUser(principal.getName());
        loginResponse.setResult(true);
        loginResponse.setUser(user);
        return loginResponse;
    }

    public LoginResponse logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        LoginResponse response = new LoginResponse();
        response.setResult(true);
        return response;
    }

    public LoginResponse login(String email, String password) {
        Authentication auth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(auth);
        LoginResponse loginResponse = new LoginResponse();

        if (!findUser(email, password)) {
            loginResponse.setResult(false);
            return loginResponse;
        }

        UserDto userDto = prepareUser(email);
        loginResponse.setResult(true);
        loginResponse.setUser(userDto);
        return loginResponse;
    }

    private UserDto prepareUser(String email) {
        User user =
                userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        UserDto userDto = dtoMapper.userToUserDTO(user);
        setSettingsAndModerationStatus(user, userDto);
        setModerationCount(userDto);

        return userDto;
    }

    private boolean findUser(String email, String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        Optional<User> user = userRepository.findByEmail(email);
        return user.filter(value -> passwordEncoder.matches(password, value.getPassword())).isPresent();
    }

    private void setModerationCount(UserDto userDto) {
        if (userDto.isModeration()) {
            int count = postRepository.findModerationPosts().size();
            userDto.setModerationCount(count);
            return;
        }
        userDto.setModerationCount(0);
    }

    private void setSettingsAndModerationStatus(User user, UserDto userDto) {
        if (user.getModerator() == 1) {
            userDto.setModeration(true);
            userDto.setSettings(true);
            return;
        }
        userDto.setSettings(false);
        userDto.setModeration(false);
    }
}

