package com.javlasov.blog.controller;

import com.javlasov.blog.api.request.CommentRequest;
import com.javlasov.blog.api.request.EditProfileRequest;
import com.javlasov.blog.api.request.ModerationRequest;
import com.javlasov.blog.api.request.SettingsRequest;
import com.javlasov.blog.api.response.*;
import com.javlasov.blog.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    private final SettingService settingService;

    private final TagService tagService;

    private final CalendarService calendarService;

    private final StorageService uploadImageService;

    private final CommentService commentService;

    private final ProfileService profileService;

    private final StatisticService statisticService;

    private final PostService postService;

    @GetMapping("/init")
    public ResponseEntity<InitResponse> init() {
        return ResponseEntity.ok(initService.init());
    }

    @GetMapping("/settings")
    public ResponseEntity<SettingsResponse> settings() {
        return ResponseEntity.ok(settingService.checkSettings());
    }

    @GetMapping("/tag")
    public ResponseEntity<TagResponse> tag(@RequestParam(required = false) Optional<String> query) {
        return ResponseEntity.ok(tagService.tag(query));
    }

    @GetMapping("/calendar")
    public ResponseEntity<CalendarResponse> calendar(@RequestParam(required = false) Optional<Integer> year) {
        return ResponseEntity.ok(calendarService.getPostsByDate(year));
    }

    @PostMapping(value = "/image", consumes = {"multipart/form-data"})
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> uploadImage(@RequestParam MultipartFile image) {
        return uploadImageService.uploadFile(image);
    }

    @PostMapping("/comment")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> sendComment(@RequestBody CommentRequest commentRequest) {
        return commentService.setCommentToPost(commentRequest.getParentId(),
                commentRequest.getPostId(),
                commentRequest.getText());
    }

    @PostMapping(value = "/profile/my", consumes = {"application/json"})
    @PreAuthorize("hasAuthority('user:write')")
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
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<StatusResponse> editMyProfileWithPhoto(
            @RequestParam(required = false) MultipartFile photo,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password) {
        return ResponseEntity.ok(profileService.editMyProfileWithPhoto(photo, name, email, password));
    }

    @GetMapping("/statistics/my")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<StatisticsResponse> getMyStatistics() {
        return ResponseEntity.ok(statisticService.getMyStatistics());
    }

    @GetMapping("/statistics/all")
    public ResponseEntity<StatisticsResponse> getAllStatistics() {
        return ResponseEntity.ok(statisticService.getAllStatistics());
    }

    @PutMapping("/settings")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<?> saveSettings(@RequestBody SettingsRequest settingsRequest) {
        return ResponseEntity.ok(settingService.saveSettings(settingsRequest));
    }

    @PostMapping("/moderation")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<StatusResponse> moderationPost(@RequestBody ModerationRequest moderationRequest) {
        return ResponseEntity.ok(postService.moderationPost(moderationRequest.getPostId(), moderationRequest.getDecision()));
    }

}
