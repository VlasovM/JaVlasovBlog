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

import java.time.LocalDate;

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
    private ResponseEntity<TagResponse> tag(@RequestParam(required = false, defaultValue = "") String query) {
        return ResponseEntity.ok(tagService.tag(query));
    }

    @GetMapping("/calendar")
    private ResponseEntity<CalendarResponse> calendar(@RequestParam(required = false, defaultValue = "0") int year) {
        return (year == 0) ? ResponseEntity.ok(calendarService.calendar(LocalDate.now().getYear()))
                : ResponseEntity.ok(calendarService.calendar(year));
    }
}
