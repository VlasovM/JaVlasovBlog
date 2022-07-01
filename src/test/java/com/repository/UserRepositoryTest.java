package com.repository;


import com.javlasov.blog.model.User;
import com.javlasov.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@RequiredArgsConstructor
public class UserRepositoryTest {

    UserRepository userRepository;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void testFindByEmail() {

        User user = new User();
        user.setEmail("memaks@mail.ru");
        user.setName("Максим");
    }

}
