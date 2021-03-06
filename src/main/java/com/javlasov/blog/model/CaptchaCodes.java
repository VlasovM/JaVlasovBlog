package com.javlasov.blog.model;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "captcha_codes")
public class CaptchaCodes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @NotNull
    @Column(name = "time")
    private LocalDateTime time;

    @NotNull
    @Column(name = "code")
    private String code;

    @NotNull
    @Column(name = "secret_code")
    private String secretCode;

}
