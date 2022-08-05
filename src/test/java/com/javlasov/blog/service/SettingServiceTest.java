package com.javlasov.blog.service;

import com.javlasov.blog.api.request.SettingsRequest;
import com.javlasov.blog.api.response.SettingsResponse;
import com.javlasov.blog.model.GlobalSettings;
import com.javlasov.blog.repository.GlobalSettingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

class SettingServiceTest {

    GlobalSettingRepository mockGlobalSettingRepo = Mockito.mock(GlobalSettingRepository.class);
    SettingService underTestService = new SettingService(mockGlobalSettingRepo);

    @Test
    @DisplayName("Check settings.")
    void checkSettingsTest() {
        SettingsResponse expectedResponse = new SettingsResponse();
        expectedResponse.setMultiuserMode(true);
        expectedResponse.setPostPremoderation(false);
        expectedResponse.setStaticsIsPublic(true);

        GlobalSettings multiUserMode = new GlobalSettings();
        multiUserMode.setCode("MULTIUSER_MODE");
        multiUserMode.setValue("YES");
        GlobalSettings postPremoderation = new GlobalSettings();
        postPremoderation.setCode("POST_PREMODERATION");
        postPremoderation.setValue("NO");
        GlobalSettings statisticsIsPublic = new GlobalSettings();
        statisticsIsPublic.setCode("STATISTICS_IS_PUBLIC");
        statisticsIsPublic.setValue("YES");

        when(mockGlobalSettingRepo.findByCode("MULTIUSER_MODE")).thenReturn(multiUserMode);
        when(mockGlobalSettingRepo.findByCode("POST_PREMODERATION")).thenReturn(postPremoderation);
        when(mockGlobalSettingRepo.findByCode("STATISTICS_IS_PUBLIC")).thenReturn(statisticsIsPublic);

        SettingsResponse actualResponse = underTestService.checkSettings();

        assertEquals(expectedResponse, actualResponse);

    }

    @Test
    @DisplayName("Save settings.")
    void saveSettingsTest() {
        ResponseEntity expectedResponse = new ResponseEntity(HttpStatus.OK);

        GlobalSettings multiUserMode = new GlobalSettings();
        multiUserMode.setCode("MULTIUSER_MODE");
        multiUserMode.setValue("YES");
        GlobalSettings postPremoderation = new GlobalSettings();
        postPremoderation.setCode("POST_PREMODERATION");
        postPremoderation.setValue("YES");
        GlobalSettings statisticsIsPublic = new GlobalSettings();
        statisticsIsPublic.setCode("STATISTICS_IS_PUBLIC");
        statisticsIsPublic.setValue("YES");

        when(mockGlobalSettingRepo.findByCode("MULTIUSER_MODE")).thenReturn(multiUserMode);
        when(mockGlobalSettingRepo.findByCode("POST_PREMODERATION")).thenReturn(postPremoderation);
        when(mockGlobalSettingRepo.findByCode("STATISTICS_IS_PUBLIC")).thenReturn(statisticsIsPublic);

        SettingsRequest request = new SettingsRequest();
        request.setMultiuserMode(false);
        request.setPostPremoderation(false);
        request.setStatisticsIsPublic(false);

        ResponseEntity<Object> actualResponse = underTestService.saveSettings(request);

        String expectedValuePostPremoderation = "NO";
        String expectedValueMultiuserMode = "NO";
        String expectedValuePostStatisticIsPublic = "NO";

        assertEquals(expectedValueMultiuserMode, multiUserMode.getValue());
        assertEquals(expectedValuePostPremoderation, postPremoderation.getValue());
        assertEquals(expectedValuePostStatisticIsPublic, statisticsIsPublic.getValue());
        assertEquals(expectedResponse, actualResponse);
    }

}