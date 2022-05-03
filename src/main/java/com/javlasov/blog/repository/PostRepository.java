package com.javlasov.blog.repository;

import com.javlasov.blog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query(value = "FROM Post WHERE active = 1 AND moderationStatus = 'ACCEPTED' AND time <= CURRENT_TIME ")
    List<Post> findAll();

    @Query(value = "FROM Post WHERE moderationStatus = 'NEW'")
    List<Post> findModerationPosts();

    List<Post> findPostsByUserId(int userId);

}
