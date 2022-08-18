package com.javlasov.blog.service;

import com.javlasov.blog.api.response.InitResponse;
import com.javlasov.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InitService {

    private final Logger logger = LoggerFactory.getLogger(InitService.class);

    public InitResponse init() {
        InitResponse initResponse = new InitResponse();
        //TODO: change this to your values
        initResponse.setTitle("JaVlasov");
        initResponse.setSubtitle("Блог о программировании и технологиях");
        initResponse.setEmail("JaVlasovM@gmail.com");
        initResponse.setCopyright("Vlasov Maxim");
        initResponse.setCopyrightFrom("2021");

        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info(currentUser + " is visited the blog");

        return initResponse;
    }

}
