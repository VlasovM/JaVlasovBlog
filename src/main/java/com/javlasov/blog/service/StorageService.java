package com.javlasov.blog.service;

import com.javlasov.blog.api.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageService {

    public ResponseEntity<?> uploadFile(MultipartFile image) {

        //only jpg and png formats
        String imageType = image.getContentType().split("/")[1];
        Map<String, String> errors = checkCorrectFormatFile(imageType);
        if (!errors.isEmpty()) {
            StatusResponse statusResponse = new StatusResponse();
            statusResponse.setErrors(errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusResponse);
        }

        String uploadPath = getPathToFile();
        uploadFile(image, uploadPath);
        return ResponseEntity.ok(uploadPath);
    }

    private Map<String, String> checkCorrectFormatFile(String typeFile) {
        Map<String, String> result = new HashMap<>();
        if (typeFile.equals("jpg") || typeFile.equals("jpeg") || typeFile.equals("png")) {
            return result;
        }
        result.put("image", "Недопустимый формат файла");
        return result;
    }

    private String getPathToFile() {
        String mainFolderName = "D:\\upload";
        String[] foldersName = UUID.randomUUID().toString().split("-");
        return mainFolderName + "\\" + foldersName[1] + "\\" +
                foldersName[2] + "\\" + foldersName[3] + "\\";
    }

    private void uploadFile(MultipartFile file, String path) {
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        try {
            File dest = new File(path + "\\" + file.getOriginalFilename());
            file.transferTo(dest);
        } catch (IOException ioException) {
            //logger
            System.out.println(ioException);
        }
    }
}
