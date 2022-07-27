package com.javlasov.blog.service;

import com.javlasov.blog.api.request.SettingsRequest;
import com.javlasov.blog.api.response.SettingsResponse;
import com.javlasov.blog.model.GlobalSettings;
import com.javlasov.blog.repository.GlobalSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettingService {

    private final GlobalSettingRepository globalSettingRepository;

    private final static String POSITIVE = "YES";
    private final static String NEGATIVE = "NO";

    public SettingsResponse checkSettings() {
        SettingsResponse settingsResponse = new SettingsResponse();

        GlobalSettings multiUserMode = globalSettingRepository.findByCode("MULTIUSER_MODE");
        GlobalSettings postPremoderation = globalSettingRepository.findByCode("POST_PREMODERATION");
        GlobalSettings statisticsIsPublic = globalSettingRepository.findByCode("STATISTICS_IS_PUBLIC");

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

    public ResponseEntity<Object> saveSettings(SettingsRequest settingsRequest) {
        GlobalSettings multiUserMode = globalSettingRepository.getById(1);
        GlobalSettings postPremoderation = globalSettingRepository.getById(2);
        GlobalSettings statisticsIsPublic = globalSettingRepository.getById(3);

        if (settingsRequest.isMultiuserMode()) {
            multiUserMode.setValue(POSITIVE);
        } else {
            multiUserMode.setValue(NEGATIVE);
        }

        if (settingsRequest.isPostPremoderation()) {
            postPremoderation.setValue(POSITIVE);
        } else {
            postPremoderation.setValue(NEGATIVE);
        }

        if (settingsRequest.isStatisticsIsPublic()) {
            statisticsIsPublic.setValue(POSITIVE);
        } else {
            statisticsIsPublic.setValue(NEGATIVE);
        }

        globalSettingRepository.save(multiUserMode);
        globalSettingRepository.save(postPremoderation);
        globalSettingRepository.save(statisticsIsPublic);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
