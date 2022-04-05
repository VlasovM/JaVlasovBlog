package com.javlasov.blog.repository;

import com.javlasov.blog.entity.Tag2Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Tag2PostRepository extends JpaRepository<Tag2Post, Integer> {
}
