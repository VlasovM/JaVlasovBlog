package com.javlasov.blog.repository;

import com.javlasov.blog.entity.GlobalSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalSettingRepository extends JpaRepository<GlobalSettings, Integer> {
}
