package com.javlasov.blog.service;

import com.javlasov.blog.api.response.CalendarResponse;
import com.javlasov.blog.constants.CommonConstants;
import com.javlasov.blog.entity.Post;
import com.javlasov.blog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final PostRepository postRepository;

    public CalendarResponse calendar(int year) {
        CalendarResponse calendarResponse = new CalendarResponse();
        List<Post> allPosts = postRepository.findAll();
        Set<Integer> years = setYears(allPosts);
        Map<String, Long> posts = findPosts(allPosts, year);

        calendarResponse.setPosts(posts);
        calendarResponse.setYears(years);
        return calendarResponse;
    }

    private Set<Integer> setYears(List<Post> allPosts) {
        Set<Integer> result = new TreeSet<>();
        for (Post post : allPosts) {
            result.add(post.getTime().getYear());
        }
        return result;
    }

    private Map<String, Long> findPosts(List<Post> allPosts, int year) {
        DateTimeFormatter formatter = CommonConstants.FORMATTER;
        List<String> dates = new ArrayList<>();
        for (Post post : allPosts) {
            if (post.getTime().getYear() == year) {
                LocalDateTime datePost = post.getTime();
                String date = datePost.format(formatter);
                dates.add(date);
            }
        }
        return dates.stream().collect(Collectors.groupingBy(
                Function.identity(), Collectors.counting()));
    }

}
