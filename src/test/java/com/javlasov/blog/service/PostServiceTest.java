package com.javlasov.blog.service;

import com.javlasov.blog.api.response.PostResponse;
import com.javlasov.blog.mappers.DtoMapper;
import com.javlasov.blog.model.Post;
import com.javlasov.blog.model.User;
import com.javlasov.blog.model.enums.ModerationStatus;
import com.javlasov.blog.repository.PostRepository;
import com.javlasov.blog.repository.Tag2PostRepository;
import com.javlasov.blog.repository.TagRepository;
import com.javlasov.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock private PostRepository postRepository;
    @Mock private TagRepository tagRepository;
    @Mock private UserRepository userRepository;
    @Mock private Tag2PostRepository tag2PostRepository;

    @Mock private DtoMapper dtoMapper;


    @BeforeEach
    void setUp() {
        postService = new PostService(postRepository, dtoMapper, tagRepository, userRepository, tag2PostRepository);
    }

    private PostService postService;

    @Test
    void getAllPosts() {
        Post post = new Post();
        post.setTime(LocalDateTime.now());
        post.setModerationStatus(ModerationStatus.ACCEPTED);
        post.setActive((short)1);

        User user = new User();
        post.setUser(user);
        postRepository.save(post);

        ArgumentCaptor<Post> postArgumentCaptor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(postArgumentCaptor.capture());
        Post value = postArgumentCaptor.getValue();

        System.out.println(value);

        System.out.println(postService.getPostById(0));
    }

    @Test
    void postSearch() {
    }

    @Test
    void getPostByDate() {
    }

    @Test
    void getPostByTag() {
    }

    @Test
    void getPostById() {
    }

    @Test
    void getMyPosts() {
    }

    @Test
    void getPostsModeration() {
    }

    @Test
    void addPost() {
    }

    @Test
    void editPost() {
    }

    @Test
    void moderationPost() {
    }
}