package com.javlasov.blog.controller;

import com.javlasov.blog.api.request.CommentRequest;
import com.javlasov.blog.api.request.EditProfileRequest;
import com.javlasov.blog.api.response.*;
import com.javlasov.blog.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
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

    private final ProfileService profileService;

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

    @PostMapping(value = "/image")
    public ResponseEntity<?> uploadImage(@RequestParam MultipartFile image) {
        return uploadImageService.uploadFile(image);
    }

    @PostMapping("/comment")
    public ResponseEntity<?> sendComment(@RequestBody CommentRequest commentRequest) {
        return commentService.setCommentToPost(commentRequest.getParentId(),
                commentRequest.getPostId(),
                commentRequest.getText());
    }

    @PostMapping(value = "/profile/my", consumes = {"application/json"})
    public ResponseEntity<StatusResponse> editMyProfileWithoutPhoto(
            @RequestBody @Valid EditProfileRequest editProfileRequest,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.ok(profileService.getRegisterWithErrors(bindingResult.getAllErrors()));
        }
        return ResponseEntity.ok(profileService.editMyProfileWithoutPhoto(
                editProfileRequest.getName(),
                editProfileRequest.getEmail(),
                editProfileRequest.getPassword(),
                editProfileRequest.getRemovePhoto()));
    }

    @PostMapping(value = "/profile/my", consumes = {"multipart/form-data"})
    public ResponseEntity<StatusResponse> editMyProfileWithPhoto(
            @RequestParam(required = false) MultipartFile photo,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password) {
        return ResponseEntity.ok(profileService.editMyProfileWithPhoto(photo, name, email, password));
    }

}
