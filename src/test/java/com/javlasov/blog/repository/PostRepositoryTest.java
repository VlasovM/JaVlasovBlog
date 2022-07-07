package com.javlasov.blog.repository;

import com.javlasov.blog.model.Post;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class PostRepositoryTest {

    private final PostRepository postRepository;

    @Test
    void findAll() {
        //given
        Post postCorrect = new Post();
        Post postInCorrent = new Post();
        postRepository.save(postCorrect);
        postRepository.save(postInCorrent);
        //when
        List<Post> allPosts = postRepository.findAll();
        //then
        assertEquals(1, allPosts);
    }

    @Test
    void findModerationPosts() {
    }

    @Test
    void findPostsByUserId() {
    }

    @Test
    void findPostsByUserIdAndAcceptedStatus() {
    }

    @Test
    void findNewPosts() {
    }

    @Test
    void findAcceptedPosts() {
    }

    @Test
    void findDeclinedPosts() {
    }
}