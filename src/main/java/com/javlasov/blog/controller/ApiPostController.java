package com.javlasov.blog.controller;

import com.javlasov.blog.api.response.PostResponse;
import com.javlasov.blog.service.PostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiPostController {

    private final PostService postService;

    public ApiPostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/post")
    public PostResponse post(@PathVariable int limit) {
        return postService.getPosts(limit);
    }
}
