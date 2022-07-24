package com.javlasov.blog.service;

import com.javlasov.blog.api.response.CalendarResponse;
import com.javlasov.blog.model.Post;
import com.javlasov.blog.model.User;
import com.javlasov.blog.model.enums.ModerationStatus;
import com.javlasov.blog.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("Return posts of 2022")
    void shouldReturnPostsByYearTest() {
        Optional<Integer> request = Optional.of(2022);

        List<Post> postsList = createPostsList();
        when(mockPostRepo.findAll()).thenReturn(postsList);

        Set<Integer> yearExpected = new HashSet<>();
        yearExpected.add(2021);
        yearExpected.add(2022);
        Map<String, Long> postsExpected = new HashMap<>();
        postsExpected.put("2022-07-19", 1L);

        CalendarResponse expected = new CalendarResponse();
        expected.setYears(yearExpected);
        expected.setPosts(postsExpected);

        CalendarResponse actual = underTestService.getPostsByDate(request);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Post repo return empty list")
    void whenPostRepoReturnEmptyListTest() {
        Optional<Integer> request = Optional.of(2022);
        List<Post> postsList = new ArrayList<>();
        when(mockPostRepo.findAll()).thenReturn(postsList);

        CalendarResponse emptyResponseExpected = new CalendarResponse();
        Set<Integer> emptySetPostsExpected = new HashSet<>();
        Map<String, Long> emptyMapPostsExpected = new HashMap<>();
        emptyResponseExpected.setPosts(emptyMapPostsExpected);
        emptyResponseExpected.setYears(emptySetPostsExpected);

        CalendarResponse actual = underTestService.getPostsByDate(request);

        assertEquals(emptyResponseExpected, actual);
    }

    @Test
    @DisplayName("Request is empty")
    void whenRequestIsEmptyTest() {
        Optional<Integer> request = Optional.empty();

       List<Post> postsList = createPostsList();

        when(mockPostRepo.findAll()).thenReturn(postsList);

        Set<Integer> yearExpected = new HashSet<>();
        yearExpected.add(2021);
        yearExpected.add(2022);
        Map<String, Long> postsExpected = new HashMap<>();
        postsExpected.put("2022-07-19", 1L);

        CalendarResponse expected = new CalendarResponse();
        expected.setYears(yearExpected);
        expected.setPosts(postsExpected);

        CalendarResponse actual = underTestService.getPostsByDate(request);

        assertEquals(expected, actual);
    }

    private List<Post> createPostsList() {

        List<Post> result = new ArrayList<>();
        Post postFirst = new Post();
        User user = new User();
        postFirst.setUser(user);
        postFirst.setActive((short) 1);
        postFirst.setTime(LocalDateTime.of(2022, 7, 19, 20, 0, 0));
        postFirst.setModerationStatus(ModerationStatus.ACCEPTED);

        Post postSecond = new Post();
        postSecond.setUser(user);
        postSecond.setActive((short) 1);
        postSecond.setTime(LocalDateTime.of(2021, 7, 19, 20, 0 ,0));
        postSecond.setModerationStatus(ModerationStatus.ACCEPTED);

        result.add(postFirst);
        result.add(postSecond);

        return result;
    }
}