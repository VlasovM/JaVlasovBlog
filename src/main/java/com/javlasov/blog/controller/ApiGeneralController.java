package com.javlasov.blog.controller;

import com.javlasov.blog.api.response.PostResponse;
import com.javlasov.blog.api.response.SettingsResponse;
import com.javlasov.blog.service.PostService;
import com.javlasov.blog.service.SettingsService;
import com.javlasov.blog.api.response.InitResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {


    private final InitResponse initResponse;
    private final SettingsService settingsService;
    private final PostService postService;

    public ApiGeneralController(InitResponse initResponse, SettingsService settingsService, PostService postService) {
        this.initResponse = initResponse;
        this.settingsService = settingsService;
        this.postService = postService;
    }

    @GetMapping("/init")
    private ResponseEntity<InitResponse> init() {
        return new ResponseEntity(initResponse, HttpStatus.OK);
    }

    @GetMapping("/settings")
    private ResponseEntity<SettingsResponse> settings() {
        return new ResponseEntity(settingsService.getGlobalSettings(), HttpStatus.OK);
    }


    @GetMapping("/post")
    private ResponseEntity<PostResponse> postResponse() {
        return new ResponseEntity<>(postService.getPostResponse(), HttpStatus.OK);
    }
}
