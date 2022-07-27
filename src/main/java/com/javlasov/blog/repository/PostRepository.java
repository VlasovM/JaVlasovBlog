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

    @Query(value = "FROM Post WHERE user.id = ?1 AND moderationStatus = 'ACCEPTED' and active = 1")
    List<Post> findPostsByUserIdAndAcceptedStatus(int userId);

    @Query(value = "FROM Post WHERE moderationStatus = 'NEW' and active = 1")
    List<Post> findNewPosts();

    @Query(value = "FROM Post WHERE moderationStatus = 'ACCEPTED' and active = 1")
    List<Post> findAcceptedPosts();

    @Query(value = "FROM Post WHERE moderationStatus = 'DECLINED' and active = 1")
    List<Post> findDeclinedPosts();

}
