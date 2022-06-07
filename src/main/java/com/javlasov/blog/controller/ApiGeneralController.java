package com.javlasov.blog.controller;

import com.javlasov.blog.api.request.CommentRequest;
import com.javlasov.blog.api.response.CalendarResponse;
import com.javlasov.blog.api.response.InitResponse;
import com.javlasov.blog.api.response.SettingsResponse;
import com.javlasov.blog.api.response.TagResponse;
import com.javlasov.blog.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiGeneralController {

    private final InitService initService;

    private final SettingService service;

    private final TagService tagService;

    private final CalendarService calendarService;

    private final StorageService uploadImageService;

    private final CommentService commentService;

    @GetMapping("/init")
    public ResponseEntity<InitResponse> init() {
        return ResponseEntity.ok(initService.init());
    }

    @GetMapping("/settings")
    public ResponseEntity<SettingsResponse> settings() {
        return ResponseEntity.ok(service.checkSetting());
    }

    @GetMapping("/tag")
    public ResponseEntity<TagResponse> tag(@RequestParam(required = false) Optional<String> query) {
        return ResponseEntity.ok(tagService.tag(query));
    }

    @GetMapping("/calendar")
    public ResponseEntity<CalendarResponse> calendar(@RequestParam(required = false) Optional<Integer> year) {
        return ResponseEntity.ok(calendarService.calendar(year));
    }

    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@RequestParam() MultipartFile image) {
        return uploadImageService.uploadFile(image);
    }

    @PostMapping("/comment")
    public ResponseEntity<?> sendComment(@RequestBody CommentRequest commentRequest) {
        return commentService.setCommentToPost(commentRequest.getParentId(),
                commentRequest.getPostId(),
                commentRequest.getText());
    }

}
