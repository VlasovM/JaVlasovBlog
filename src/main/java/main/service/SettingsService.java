package main.service;

import main.api.response.SettingsResponse;
import main.repository.GlobalSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {

    @Autowired
    private GlobalSettingsRepository globalSettingsRepository;

    public SettingsResponse getGlobalSettings() {
        SettingsResponse settingsResponse = new SettingsResponse();
        String multiUserModeValue = globalSettingsRepository.findById(1).get().getValue();
        String postPremoderationValue = globalSettingsRepository.findById(2).get().getValue();
        String statisticsIsPublicValue = globalSettingsRepository.findById(3).get().getValue();

        switch (multiUserModeValue) {
            case ("YES"):
                settingsResponse.setMultiuserMode(true);
                break;
            case ("NO"):
                settingsResponse.setMultiuserMode(false);
                break;
        }
        switch (postPremoderationValue) {
            case ("YES"):
                settingsResponse.setPostPremoderation(true);
                break;
            case ("NO"):
                settingsResponse.setPostPremoderation(false);
                break;
        }
        switch (statisticsIsPublicValue) {
            case ("YES"):
                settingsResponse.setStatisticsIsPublic(true);
                break;
            case ("NO"):
                settingsResponse.setStatisticsIsPublic(false);
                break;
        }
        return settingsResponse;
    }
}
