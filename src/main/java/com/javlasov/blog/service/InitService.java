package com.javlasov.blog.service;

import com.javlasov.blog.api.response.InitResponse;
import org.springframework.stereotype.Service;

@Service
public class InitService {

    public InitResponse init() {
        InitResponse initResponse = new InitResponse();
        initResponse.setTitle("JaVlasov");
        initResponse.setSubtitle("Блог о программировании и технологиях");
        initResponse.setEmail("JaVlasovM@gmail.com");
        initResponse.setCopyright("Vlasov Maxim");
        initResponse.setCopyrightFrom("2021");
        return initResponse;
    }

}
