package com.javlasov.blog.service;

import com.javlasov.blog.api.response.StatusResponse;
import com.javlasov.blog.model.User;
import com.javlasov.blog.repository.CaptchaRepository;
import com.javlasov.blog.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.mail.*;
import javax.mail.search.SearchTerm;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PasswordServiceTest {

    UserRepository mockUserRepo = Mockito.mock(UserRepository.class);
    CaptchaRepository mockCaptchaRepo = Mockito.mock(CaptchaRepository.class);
    PasswordService underTestService = new PasswordService(mockUserRepo, mockCaptchaRepo);


    @Test
    @DisplayName("Restore password from existing User")
    void restorePasswordFromExistingUser() {
        StatusResponse expected = new StatusResponse();
        expected.setResult(true);
        User user = getUser();

        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        StatusResponse actual = underTestService.restorePassword(user.getEmail());
        boolean actualLastEmail = checkLastEmail();

        assertEquals(expected, actual);
        assertTrue(actualLastEmail);

    }

    @Test
    void changePassword() {

    }

    private boolean checkLastEmail() {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.host", "smtp.mail.ru");
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.ssl.trust", "smtp.mail.ru");
        props.put("mail.smtp.starttls.enable", "true");


        try {
            Session session = Session.getInstance(props, null);
            Store store = session.getStore();
            store.connect("imap.mail.ru", "memaks@mail.ru", "38-29-41-12-7M");
            Folder folder = store.getFolder("Входящие");
            folder.open(Folder.READ_ONLY);
            Message[] messages = folder.getMessages();
            System.out.println(messages[1]);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    private User getUser() {
        User user = new User();
        user.setName("Maxim");
        user.setEmail("memaks@mail.ru");
        user.setModerator(0);
        return user;
    }

}