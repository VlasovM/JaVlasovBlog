package com.javlasov.blog.controller;

import com.javlasov.blog.api.response.InitResponse;
import com.javlasov.blog.api.response.SettingsResponse;
import com.javlasov.blog.service.SettingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {


    private final InitResponse initResponse;
    private final SettingService service;

    public ApiGeneralController(InitResponse initResponse, SettingService service) {
        this.initResponse = initResponse;
        this.service = service;
    }

    @GetMapping("/init")
    private InitResponse init() {
        return initResponse;
    }

    @GetMapping("/settings")
    private SettingsResponse settings() {
        return service.checkSetting();
    }
}
