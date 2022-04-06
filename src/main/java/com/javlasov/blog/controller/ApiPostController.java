package com.javlasov.blog.controller;

import com.javlasov.blog.api.response.PostResponse;
import com.javlasov.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiPostController {

    private final PostService postService;

    @GetMapping("/post")
    public ResponseEntity<PostResponse> post(@RequestParam String mode, @RequestParam Integer offset, @RequestParam Integer limit) {
        if (offset == null) {
            offset = 0;
        }
        if (limit == null) {
            limit = 10;
        }
        return ResponseEntity.ok(postService.postResponse(mode, offset, limit));
    }

}
