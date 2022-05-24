package com.javlasov.blog.controller;

import com.javlasov.blog.api.request.ModerationRequest;
import com.javlasov.blog.api.request.PostRequest;
import com.javlasov.blog.api.response.PostResponse;
import com.javlasov.blog.api.response.RegisterResponse;
import com.javlasov.blog.dto.PostDtoById;
import com.javlasov.blog.model.Tag;
import com.javlasov.blog.model.Tag2Post;
import com.javlasov.blog.repository.PostRepository;
import com.javlasov.blog.repository.Tag2PostRepository;
import com.javlasov.blog.repository.TagRepository;
import com.javlasov.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiPostController {

    private final PostService postService;

    private final PostRepository postRepository;

    private final Tag2PostRepository tag2PostRepository;

    private final TagRepository tagRepository;

    @GetMapping("/post")
    public ResponseEntity<PostResponse> post(@RequestParam(required = false, defaultValue = "recent") String mode,
                                             @RequestParam(required = false, defaultValue = "0") int offset,
                                             @RequestParam(required = false, defaultValue = "10") int limit) {
        return ResponseEntity.ok(postService.getAllPosts(mode, offset, limit));
    }

    @GetMapping("/post/search")
    public ResponseEntity<PostResponse> postSearch(@RequestParam String query,
                                                   @RequestParam(required = false, defaultValue = "0") int offset,
                                                   @RequestParam(required = false, defaultValue = "10") int limit) {
        String str = query.trim();
        return (str.isEmpty()) ? ResponseEntity.ok(postService.getAllPosts("recent", offset, limit)) :
                ResponseEntity.ok(postService.postSearch(query, offset, limit));
    }

    @GetMapping("/post/byDate")
    public ResponseEntity<PostResponse> postByDate(@RequestParam String date,
                                                   @RequestParam(required = false, defaultValue = "0") int offset,
                                                   @RequestParam(required = false, defaultValue = "10") int limit) {
        return ResponseEntity.ok(postService.getPostByDate(date, offset, limit));
    }

    @GetMapping("/post/byTag")
    public ResponseEntity<PostResponse> postByTag(@RequestParam String tag,
                                                  @RequestParam(required = false, defaultValue = "0") int offset,
                                                  @RequestParam(required = false, defaultValue = "10") int limit) {
        return ResponseEntity.ok(postService.getPostByTag(tag, offset, limit));
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<PostDtoById> postById(@PathVariable int id) {
        return (postRepository.existsById(id)) ?
                ResponseEntity.ok(postService.getPostById(id)) :
                ResponseEntity.notFound().build();
    }

    @GetMapping("/post/my")
    public ResponseEntity<PostResponse> getMyPosts(@RequestParam String status,
                                                   @RequestParam(required = false, defaultValue = "0") int offset,
                                                   @RequestParam(required = false, defaultValue = "10") int limit) {
        return ResponseEntity.ok(postService.getMyPosts(status, offset, limit));
    }

    @GetMapping("/post/moderation")
    public ResponseEntity<PostResponse> getPostsModeration(
            @RequestParam String status,
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "10") int limit) {
        return ResponseEntity.ok(postService.getPostsModeration(status, offset, limit));
    }

    @PostMapping("/post")
    public ResponseEntity<RegisterResponse> addPost(@RequestBody PostRequest postRequest) {
       return ResponseEntity.ok(postService.addPost(postRequest.getTimestamp(), postRequest.getActive(),
                postRequest.getTitle(), postRequest.getTags(), postRequest.getText()));
    }

    @PutMapping("/post/{id}")
    public ResponseEntity<RegisterResponse> editPost(@PathVariable int id,
                                                     @RequestBody PostRequest postRequest) {
        return ResponseEntity.ok(postService.editPost(id, postRequest.getTimestamp(), postRequest.getActive(),
                postRequest.getTitle(), postRequest.getTags(), postRequest.getText()));
    }

    @PostMapping("moderation")
    public ResponseEntity<RegisterResponse> moderationPost(@RequestBody ModerationRequest moderationRequest) {
        return ResponseEntity.ok(postService.moderationPost(moderationRequest.getPostId(), moderationRequest.getDecision()));
    }

    @GetMapping("/post/test")
    public ResponseEntity<RegisterResponse> test() {
        return null;
    }
}
