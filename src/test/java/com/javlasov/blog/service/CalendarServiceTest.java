package com.javlasov.blog.service;

import com.javlasov.blog.api.response.CalendarResponse;
import com.javlasov.blog.model.Post;
import com.javlasov.blog.model.User;
import com.javlasov.blog.model.enums.ModerationStatus;
import com.javlasov.blog.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CalendarServiceTest {

    PostRepository mockPostRepo = Mockito.mock(PostRepository.class);
    CalendarService underTestService = new CalendarService(mockPostRepo);

    @Test
    void getPostsByDate() {
        Optional<Integer> request = Optional.of(2022);

        List<Post> postsList = new ArrayList<>();

        Post postFirst = new Post();
        User user = new User();
        postFirst.setUser(user);
        postFirst.setActive((short) 1);
        postFirst.setTime(LocalDateTime.now());
        postFirst.setModerationStatus(ModerationStatus.ACCEPTED);

        Post postSecond = new Post();
        postSecond.setUser(user);
        postSecond.setActive((short) 1);
        postSecond.setTime(LocalDateTime.now().minusYears(1));
        postSecond.setModerationStatus(ModerationStatus.ACCEPTED);

        postsList.add(postFirst);
        postsList.add(postSecond);

        when(mockPostRepo.findAll()).thenReturn(postsList);

        CalendarResponse expected = new CalendarResponse();
        Set<Integer> yearExpected = new HashSet<>();
        yearExpected.add(2021);
        yearExpected.add(2022);
        Map<String, Long> postsExpected = new HashMap<>();
        postsExpected.put("2022-07-19", 1L);
//        postsExpected.put("2021-19-07", 1L);
        expected.setYears(yearExpected);
        expected.setPosts(postsExpected);
        CalendarResponse actual = underTestService.getPostsByDate(request);

        assertEquals(expected, actual);
    }
}