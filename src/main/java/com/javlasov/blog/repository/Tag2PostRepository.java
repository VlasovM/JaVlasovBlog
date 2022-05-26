package com.javlasov.blog.repository;

import com.javlasov.blog.model.Tag2Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Tag2PostRepository extends JpaRepository<Tag2Post, Integer> {

    List<Tag2Post> findByTagId(int tagId);

}
