package com.javlasov.blog.controller;

import com.javlasov.blog.api.response.PostResponse;
import com.javlasov.blog.dto.PostDto;
import com.javlasov.blog.entity.Post;
import com.javlasov.blog.entity.PostVotes;
import com.javlasov.blog.mappers.PostMapper;
import com.javlasov.blog.repository.PostRepository;
import com.javlasov.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiPostController {

    private final PostRepository postRepository;

    private final PostService postService;

    private final PostMapper postMapper;

    @GetMapping("/post")
    public PostResponse post() {
        return postService.postResponse();
    }

}
