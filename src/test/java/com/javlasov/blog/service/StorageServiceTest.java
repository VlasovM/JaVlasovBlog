package com.javlasov.blog.service;

import com.javlasov.blog.api.response.StatusResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StorageServiceTest {

    StorageService underTestService = new StorageService();

    //Change these variables to their own values
    private final String PATH_TO_FILE_PDF = "C:\\Users\\79153\\Desktop\\Other\\bill.pdf";

    @Test
    @DisplayName("Test incorrect format file")
    void uploadFileIncorrectFormatTest() throws IOException {

        StatusResponse statusResponse = new StatusResponse();
        Map<String, String> errorsMapExpected = new HashMap<>();
        errorsMapExpected.put("image", "Недопустимый формат файла");
        statusResponse.setErrors(errorsMapExpected);

        ResponseEntity<StatusResponse> expectedResponse = ResponseEntity.badRequest().body(statusResponse);

        FileInputStream fis = new FileInputStream(PATH_TO_FILE_PDF);
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "JpgFile", "bill", "application/pdf", fis);

        ResponseEntity<?> actualResponse = underTestService.uploadFile(mockMultipartFile);

        assertEquals(expectedResponse, actualResponse);

        fis.close();
    }

}