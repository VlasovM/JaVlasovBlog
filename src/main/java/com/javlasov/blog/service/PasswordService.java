package com.javlasov.blog.service;

import com.javlasov.blog.api.response.StatusResponse;
import com.javlasov.blog.model.User;
import com.javlasov.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.net.InetAddress;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private final UserRepository userRepository;

    public StatusResponse restorePassword(String email) {
        StatusResponse statusResponse = new StatusResponse();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return statusResponse;
        }

        String hash = UUID.randomUUID().toString().replaceAll("-", "");
        user.get().setCode(hash);
        sendEmail(email, hash);
        userRepository.save(user.get());
        statusResponse.setResult(true);
        return statusResponse;
    }

    public StatusResponse changePassword(String code, String password, String captcha, String captchaSecret) {
        StatusResponse statusResponse = new StatusResponse();
        return statusResponse;
    }

    private void sendEmail(String emailTo, String hash) {

        Properties props = getProperties();
        Session session = getSession(props);

        Message message = new MimeMessage(session);

        try {
            InternetAddress emailFrom = new InternetAddress("m.a.vlasov97@gmail.com");
            message.setFrom(emailFrom);
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(emailTo));

            String linkRestore = InetAddress.getLoopbackAddress().getHostName() + "/login/change-password/" + hash;

            message.setSubject("Restore password in JaVlasovBlog");
            String msg = "Hello! Your link to restore password in JaVlasovBlog: " + "<a>" + linkRestore + "<a>";

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            message.setContent(multipart);

            Transport.send(message);

        } catch (Exception exception) {
            //TODO: logger
            exception.printStackTrace();
        }
    }

    private Properties getProperties() {
        Properties props = new Properties();
        props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.starttls.enable", "true");
        return props;
    }

    private Session getSession(Properties props) {
        String username = "m.a.vlasov97@gmail.com";
        //https://support.google.com/mail/answer/185833?hl=en-GB
        String password = "ufblfmxxfafcqmto";

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }
}
