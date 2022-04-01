package com.javlasov.blog.service;

import com.javlasov.blog.api.response.SettingsResponse;
import com.javlasov.blog.entity.GlobalSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Service
public class SettingService {

    @Autowired
    private EntityManager entityManager;

    public SettingsResponse checkSetting() {
        SettingsResponse settingsResponse = new SettingsResponse();
        GlobalSettings multiUserMode = getGlobalSettings(1);
        GlobalSettings postPremoderation = getGlobalSettings(2);
        GlobalSettings statisticsIsPublic = getGlobalSettings(3);

        switch (multiUserMode.getValue()) {
            case ("YES"):
                settingsResponse.setMultiuserMode(true);
                break;
            case ("NO"):
                settingsResponse.setMultiuserMode(false);
                break;
        }

        switch (postPremoderation.getValue()) {
            case ("YES"):
                settingsResponse.setPostPremoderation(true);
                break;
            case ("NO"):
                settingsResponse.setPostPremoderation(false);
                break;
        }

        switch (statisticsIsPublic.getValue()) {
            case ("YES"):
                settingsResponse.setStaticsIsPublic(true);
                break;
            case ("NO"):
                settingsResponse.setStaticsIsPublic(false);
                break;
        }


        return settingsResponse;
    }

    public GlobalSettings getGlobalSettings(int id) {
        Query query = entityManager.createQuery("from GlobalSettings");
        List<GlobalSettings> globalSettingsList = query.getResultList();
        GlobalSettings globalSettings = globalSettingsList.get(id - 1);
        return globalSettings;
    }
}
