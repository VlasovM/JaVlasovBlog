package com.javlasov.blog.service;

import com.javlasov.blog.api.response.StatusResponse;
import com.javlasov.blog.model.User;
import com.javlasov.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(ProfileService.class);

    public StatusResponse editMyProfileWithoutPhoto(String name, String email, String password, int removePhoto) {
        StatusResponse statusResponse = new StatusResponse();
        User user = getCurrentAuthorizedUser();
        user.setName(name);
        user.setEmail(email);
        //change password
        if (!(password == null)) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
            user.setPassword(encoder.encode(password));
        }
        //delete photo
        if (removePhoto == 1) {
            user.setPhoto(null);
        }
        userRepository.save(user);
        statusResponse.setResult(true);
        return statusResponse;
    }

    private User getCurrentAuthorizedUser() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(userEmail).orElseThrow();
    }

    public StatusResponse editMyProfileWithPhoto(MultipartFile photo, String name, String email,
                                                 String password) {
        StatusResponse statusResponse = new StatusResponse();
        User user = getCurrentAuthorizedUser();
        user.setName(name);
        user.setEmail(email);
        try {
            user.setPhoto(uploadFile(photo));
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        if (!(password == null)) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
            user.setPassword(encoder.encode(password));
        }
        userRepository.save(user);
        statusResponse.setResult(true);
        return statusResponse;
    }

    //Checking at the controller level using your own annotations
    public StatusResponse getRegisterWithErrors(List<ObjectError> listErrors) {
        StatusResponse response = new StatusResponse();
        Map<String, String> errors = new HashMap<>();
        listErrors.forEach((error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        }));
        response.setResult(false);
        response.setErrors(errors);
        return response;
    }

    private Path getPathToFile() {
        String[] foldersName = UUID.randomUUID().toString().split("-");
        return Paths.get("\\upload\\profile" + "\\" + foldersName[1] + "\\" +
                foldersName[2] + "\\" + foldersName[3] + "\\");
    }

    private String uploadFile(MultipartFile file) throws IOException {
        String imageType = file.getContentType().split("/")[1];
        int maxPhotoSize = 128;
        Path path = getPathToFile();
        BufferedImage image = ImageIO.read(file.getInputStream());
        int height = (int) (Math.round(image.getHeight()) / (image.getWidth() / (double) maxPhotoSize));
        BufferedImage newImage = Scalr.resize(
                image,
                Scalr.Method.AUTOMATIC,
                Scalr.Mode.AUTOMATIC,
                maxPhotoSize,
                height,
                Scalr.OP_ANTIALIAS);

        if (!new File(path.toString()).exists()) {
            new File(path.toString()).mkdirs();
        }

        File newFile = new File(path + "\\" + file.getOriginalFilename());
        ImageIO.write(newImage, imageType, newFile);
        logger.info("File has been successfully saved in: {}", newFile.getPath());
        return newFile.getPath();
    }

}
