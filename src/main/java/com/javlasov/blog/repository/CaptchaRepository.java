package com.javlasov.blog.repository;

import com.javlasov.blog.entity.CaptchaCodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaptchaRepository extends JpaRepository<CaptchaCodes, Integer> {

    CaptchaCodes findBySecretCode(String code);
    boolean existsBySecretCode(String code);

}
