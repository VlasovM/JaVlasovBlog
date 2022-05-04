package com.javlasov.blog.controller;

import com.javlasov.blog.api.response.CalendarResponse;
import com.javlasov.blog.api.response.InitResponse;
import com.javlasov.blog.api.response.SettingsResponse;
import com.javlasov.blog.api.response.TagResponse;
import com.javlasov.blog.service.CalendarService;
import com.javlasov.blog.service.InitService;
import com.javlasov.blog.service.SettingService;
import com.javlasov.blog.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiGeneralController {

    private final InitService initService;
    private final SettingService service;
    private final TagService tagService;
    private final CalendarService calendarService;

    @GetMapping("/init")
    private ResponseEntity<InitResponse> init() {
        return ResponseEntity.ok(initService.init());
    }

    @GetMapping("/settings")
    private ResponseEntity<SettingsResponse> settings() {
        return ResponseEntity.ok(service.checkSetting());
    }

    @GetMapping("/tag")
    private ResponseEntity<TagResponse> tag(@RequestParam(required = false) Optional<String> query) {
        return ResponseEntity.ok(tagService.tag(query));
    }

    @GetMapping("/calendar")
    private ResponseEntity<CalendarResponse> calendar(@RequestParam(required = false) Optional<Integer> year) {
        return ResponseEntity.ok(calendarService.calendar(year));
    }
}
