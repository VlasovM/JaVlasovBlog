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
    public ResponseEntity<PostResponse> post(@RequestParam(required = false, defaultValue = "recent") String mode,
                                             @RequestParam(required = false, defaultValue = "0") int offset,
                                             @RequestParam(required = false, defaultValue = "10") int limit) {
        return ResponseEntity.ok(postService.getAllPosts(mode, offset, limit));
    }

    @GetMapping("post/search")
    public ResponseEntity<PostResponse> postSearch(@RequestParam String query,
                                                   @RequestParam(required = false, defaultValue = "0") int offset,
                                                   @RequestParam(required = false, defaultValue = "10") int limit) {
        String str = query.trim();
        return (str.isEmpty()) ? ResponseEntity.ok(postService.getAllPosts("recent", offset, limit)) :
                ResponseEntity.ok(postService.postSearch(query, offset, limit));
    }

    @GetMapping("post/byDate")
    public ResponseEntity<PostResponse> postByDate(@RequestParam String date,
                                                   @RequestParam(required = false, defaultValue = "0") int offset,
                                                   @RequestParam(required = false, defaultValue = "10") int limit) {
        return ResponseEntity.ok(postService.getPostByDate(date, offset, limit));
    }

    @GetMapping("post/byTag")
    public ResponseEntity<PostResponse> postByTag(@RequestParam String tag,
                                                  @RequestParam(required = false, defaultValue = "0") int offset,
                                                  @RequestParam(required = false, defaultValue = "10") int limit) {
        return ResponseEntity.ok(postService.getPostByTag(tag, offset, limit));
    }
}
