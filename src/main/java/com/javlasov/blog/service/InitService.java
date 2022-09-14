package com.javlasov.blog.service;

import com.javlasov.blog.api.response.InitResponse;
import com.javlasov.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InitService {

    @Value("${init.title}")
    private String title;

    @Value("${init.subtitle}")
    private String subTitle;

    @Value("${init.email}")
    private String email;

    @Value("${init.copyRight}")
    private String copyright;

    @Value("${init.CopyrightFrom}")
    private String copyrightFrom;

    private final Logger logger = LoggerFactory.getLogger(InitService.class);

    public InitResponse init() {
        InitResponse initResponse = new InitResponse();
        initResponse.setTitle(title);
        initResponse.setSubtitle(subTitle);
        initResponse.setEmail(email);
        initResponse.setCopyright(copyright);
        initResponse.setCopyrightFrom(copyrightFrom);

        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info(currentUser + " is visited the blog");

        return initResponse;
    }

}
