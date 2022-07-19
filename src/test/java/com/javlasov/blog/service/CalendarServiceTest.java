package com.javlasov.blog.service;

import com.javlasov.blog.api.response.CalendarResponse;
import com.javlasov.blog.model.Post;
import com.javlasov.blog.model.enums.ModerationStatus;
import com.javlasov.blog.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CalendarServiceTest {

    @Mock private PostRepository postRepository;
    private CalendarService calendarService;

    @BeforeEach
    void setUp() {
        calendarService = new CalendarService(postRepository);
    }

    @Test
    void canGetPostsByDate() {
        Post post = new Post();
        post.setTime(LocalDateTime.now());
        post.setModerationStatus(ModerationStatus.ACCEPTED);
        post.setActive((short)1);
        postRepository.save(post);
        ArgumentCaptor<Post> postArgumentCaptor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(postArgumentCaptor.capture());
        Post value = postArgumentCaptor.getValue();
        System.out.println(value);
        System.out.println(calendarService.getPostsByDate(Optional.of(2022)));
    }
}