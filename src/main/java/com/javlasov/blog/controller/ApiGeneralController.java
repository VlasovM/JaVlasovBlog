package com.javlasov.blog.controller;

import com.javlasov.blog.api.response.InitResponse;
import com.javlasov.blog.api.response.SettingsResponse;
import com.javlasov.blog.api.response.TagResponse;
import com.javlasov.blog.service.SettingService;
import com.javlasov.blog.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final SettingService service;
    private final TagService tagService;

    @GetMapping("/init")
    private ResponseEntity<InitResponse> init() {
        return ResponseEntity.ok(initResponse);
    }

    @GetMapping("/settings")
    private ResponseEntity<SettingsResponse> settings() {
        return ResponseEntity.ok(service.checkSetting());
    }

    @GetMapping("/tag")
    private ResponseEntity<TagResponse> tag(@RequestParam String query) {
        return ResponseEntity.ok(tagService.tag(query));
    }
}
