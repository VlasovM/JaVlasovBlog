package com.javlasov.blog.controller;

import com.javlasov.blog.api.request.PostRequest;
import com.javlasov.blog.api.request.VotesRequest;
import com.javlasov.blog.api.response.PostResponse;
import com.javlasov.blog.api.response.StatusResponse;
import com.javlasov.blog.dto.PostDtoById;
import com.javlasov.blog.repository.PostRepository;
import com.javlasov.blog.service.PostService;
import com.javlasov.blog.service.VotesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class ApiPostController {

    private final PostService postService;

    private final PostRepository postRepository;

    private final VotesService votesService;

    @GetMapping()
    public ResponseEntity<PostResponse> post(@RequestParam(required = false, defaultValue = "recent") String mode,
                                             @RequestParam(required = false, defaultValue = "0") int offset,
                                             @RequestParam(required = false, defaultValue = "10") int limit) {
        return ResponseEntity.ok(postService.getAllPosts(mode, offset, limit));
    }

    @GetMapping("/search")
    public ResponseEntity<PostResponse> postSearch(@RequestParam String query,
                                                   @RequestParam(required = false, defaultValue = "0") int offset,
                                                   @RequestParam(required = false, defaultValue = "10") int limit) {
        String str = query.trim();
        return (str.isEmpty()) ? ResponseEntity.ok(postService.getAllPosts("recent", offset, limit)) :
                ResponseEntity.ok(postService.postSearch(query, offset, limit));
    }

    @GetMapping("/byDate")
    public ResponseEntity<PostResponse> postByDate(@RequestParam String date,
                                                   @RequestParam(required = false, defaultValue = "0") int offset,
                                                   @RequestParam(required = false, defaultValue = "10") int limit) {
        return ResponseEntity.ok(postService.getPostByDate(date, offset, limit));
    }

    @GetMapping("/byTag")
    public ResponseEntity<PostResponse> postByTag(@RequestParam String tag,
                                                  @RequestParam(required = false, defaultValue = "0") int offset,
                                                  @RequestParam(required = false, defaultValue = "10") int limit) {
        return ResponseEntity.ok(postService.getPostByTag(tag, offset, limit));
    }

    @GetMapping("/moderation")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<PostResponse> getListModerationPosts(@RequestParam String status,
                                                               @RequestParam(required = false, defaultValue = "0") int offset,
                                                               @RequestParam(required = false, defaultValue = "10") int limit) {
        return ResponseEntity.ok(postService.getPostsModeration(status, offset, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDtoById> postById(@PathVariable int id) {
        return (postRepository.existsById(id)) ?
                ResponseEntity.ok(postService.getPostById(id)) :
                ResponseEntity.notFound().build();
    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<PostResponse> getMyPosts(@RequestParam String status,
                                                   @RequestParam(required = false, defaultValue = "0") int offset,
                                                   @RequestParam(required = false, defaultValue = "10") int limit) {
        return ResponseEntity.ok(postService.getMyPosts(status, offset, limit));
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<StatusResponse> addPost(@RequestBody PostRequest postRequest) {
        return ResponseEntity.ok(postService.addPost(postRequest.getTimestamp(), postRequest.getActive(),
                postRequest.getTitle(), postRequest.getTags(), postRequest.getText()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<StatusResponse> editPost(@PathVariable int id,
                                                   @RequestBody PostRequest postRequest) {
        return ResponseEntity.ok(postService.editPost(id, postRequest.getTimestamp(), postRequest.getActive(),
                postRequest.getTitle(), postRequest.getTags(), postRequest.getText()));
    }

    @PostMapping("/like")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<StatusResponse> setLike(@RequestBody VotesRequest votesRequest) {
        return ResponseEntity.ok(votesService.setLike(votesRequest.getPostId()));
    }

    @PostMapping("dislike")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<StatusResponse> setDislike(@RequestBody VotesRequest votesRequest) {
        return ResponseEntity.ok(votesService.setDislike(votesRequest.getPostId()));
    }

}
