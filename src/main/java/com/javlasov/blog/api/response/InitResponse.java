package com.javlasov.blog.api.response;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("blog")
public class InitResponse {
    private String title;
    private String subTitle;
    private String phone;
    private String email;
    private String copyRight;
    private String copyrightFrom;
}
