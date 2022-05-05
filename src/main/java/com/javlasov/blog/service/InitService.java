package com.javlasov.blog.service;

import com.javlasov.blog.api.response.InitResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class InitService {

    public InitResponse init() {
        InitResponse initResponse = new InitResponse();
        initResponse.setTitle("JaVlasov");
        initResponse.setSubTitle("JaVlasov Blog");
        initResponse.setPhone("+7(915)-330-47-61");
        initResponse.setEmail("m.a.vlasov97@gmail.com");
        initResponse.setCopyRight("Vlasov Maxim");
        initResponse.setCopyrightFrom("2021");
        return initResponse;
    }

}
