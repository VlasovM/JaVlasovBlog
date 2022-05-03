package com.javlasov.blog.service;

import com.javlasov.blog.api.response.SettingsResponse;
import com.javlasov.blog.model.GlobalSettings;
import com.javlasov.blog.repository.GlobalSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettingService {

    private final GlobalSettingRepository globalSettingRepository;
    private final static String POSITIVE = "YES";
    private final static String NEGATIVE = "NO";

    public SettingsResponse checkSetting() {
        SettingsResponse settingsResponse = new SettingsResponse();
        GlobalSettings multiUserMode = globalSettingRepository.getById(1);
        GlobalSettings postPremoderation = globalSettingRepository.getById(2);
        GlobalSettings statisticsIsPublic = globalSettingRepository.getById(3);

        switch (multiUserMode.getValue()) {
            case (POSITIVE):
                settingsResponse.setMultiuserMode(true);
                break;
            case (NEGATIVE):
                settingsResponse.setMultiuserMode(false);
                break;
        }

        switch (postPremoderation.getValue()) {
            case (POSITIVE):
                settingsResponse.setPostPremoderation(true);
                break;
            case (NEGATIVE):
                settingsResponse.setPostPremoderation(false);
                break;
        }

        switch (statisticsIsPublic.getValue()) {
            case (POSITIVE):
                settingsResponse.setStaticsIsPublic(true);
                break;
            case (NEGATIVE):
                settingsResponse.setStaticsIsPublic(false);
                break;
        }


        return settingsResponse;
    }
}
