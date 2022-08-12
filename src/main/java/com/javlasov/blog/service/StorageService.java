package com.javlasov.blog.service;

import com.javlasov.blog.api.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final Logger logger = LoggerFactory.getLogger(StorageService.class);

    public ResponseEntity<?> uploadFile(MultipartFile image) {

        //only jpg and png formats
        String imageType = image.getContentType().split("/")[1];
        Map<String, String> errors = checkCorrectFormatFile(imageType);

        if (!errors.isEmpty()) {
            StatusResponse statusResponse = new StatusResponse();
            statusResponse.setErrors(errors);
            logger.info("Image was not upload: {}", errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusResponse);
        }

        String path = "";
        try {
            path = uploadFileAndGetPath(image);
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        logger.info("Image has been successfully saved in: {}", path);
        return ResponseEntity.ok(path);
    }

    private Map<String, String> checkCorrectFormatFile(String typeFile) {
        Map<String, String> result = new HashMap<>();
        if (typeFile.equals("jpg") || typeFile.equals("jpeg") || typeFile.equals("png")) {
            return result;
        }
        result.put("image", "Недопустимый формат файла");
        return result;
    }

    private Path getPathToFile() {
        String[] foldersName = UUID.randomUUID().toString().split("-");
        return Paths.get("\\upload\\image" + "\\" + foldersName[1] + "\\" +
                foldersName[2] + "\\" + foldersName[3] + "\\");
    }

    private String uploadFileAndGetPath(MultipartFile file) throws IOException {
        String imageType = file.getContentType().split("/")[1];
        BufferedImage image = ImageIO.read(file.getInputStream());
        int maxPhotoSize = 350; //px
        int height = (int) (Math.round(image.getHeight()) / (image.getWidth() / (double) maxPhotoSize));
        BufferedImage newImage = Scalr.resize(
                image,
                Scalr.Method.AUTOMATIC,
                Scalr.Mode.AUTOMATIC,
                maxPhotoSize,
                height,
                Scalr.OP_ANTIALIAS);

        Path path = getPathToFile();

        if (!new File(path.toString()).exists()) {
            new File(path.toString()).mkdirs();
        }

        String fileType = file.getOriginalFilename().split("\\.")[1];
        String fileName = RandomString.make(12) + "." + fileType;

        File newFile = new File(path + "\\" + fileName);
        ImageIO.write(newImage, imageType, newFile);
        return newFile.getPath();
    }

}
