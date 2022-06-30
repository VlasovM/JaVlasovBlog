package com.repository;


import com.javlasov.blog.model.User;
import com.javlasov.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
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

        Assert.assertNotNull(userRepository.findByEmail("memaks@mail.ru"));
    }

}
