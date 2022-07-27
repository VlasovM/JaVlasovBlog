package com.javlasov.blog.service;

import com.javlasov.blog.api.response.InitResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InitService {

    private final Logger logger = LoggerFactory.getLogger(InitService.class);

    public InitResponse init() {
        InitResponse initResponse = new InitResponse();
        initResponse.setTitle("JaVlasov");
        initResponse.setSubtitle("Блог о программировании и технологиях");
        initResponse.setEmail("JaVlasovM@gmail.com");
        initResponse.setCopyright("Vlasov Maxim");
        initResponse.setCopyrightFrom("2021");
        logger.info("Anonymous user visited the site");
        return initResponse;
    }

}
