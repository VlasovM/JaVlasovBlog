package com.javlasov.blog.service;

import com.javlasov.blog.api.response.StatusResponse;
import com.javlasov.blog.model.User;
import com.javlasov.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;

    private final int MAX_WEIGHT_AND_WIGHT_PHOTO = 36; //pixels

    public StatusResponse editMyProfileWithoutPhoto(String name, String email, String password) {
        StatusResponse statusResponse = new StatusResponse();

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail).get();
        user.setName(name);
        user.setEmail(email);
        if (!(password == null)) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
            user.setPassword(encoder.encode(password));
            userRepository.save(user);
            statusResponse.setResult(true);
            return statusResponse;
        }
        userRepository.save(user);
        statusResponse.setResult(true);
        return statusResponse;

    }

    public StatusResponse editMyProfileWithPhoto(MultipartFile photo, String name, String email,
                                                 String password, int removePhoto) {
        StatusResponse statusResponse = new StatusResponse();

        if (password == null) {
            //method chane only photo and email \ name + valid
        }

        if (photo.isEmpty() && removePhoto == 1) {

        }


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

    private void changePhoto(MultipartFile photo, String email, String name) {
        String pathToFile = getPathToFile();
    }

    private void changePhotoAndPassword(MultipartFile photo, String email, String name, String password) {

    }

    private void deleteUserPhoto(String email, String name) {

    }

    private String getPathToFile() {
        String mainFolderName = "D:\\upload";
        String[] foldersName = UUID.randomUUID().toString().split("-");
        return mainFolderName + "\\" + foldersName[1] + "\\" +
                foldersName[2] + "\\" + foldersName[3] + "\\";
    }

    private void uploadFile(MultipartFile file) {
        String pathToFile = getPathToFile();
        String imageType = file.getContentType().split("/")[1];
        BufferedImage image = ImageIO.read(file.getInputStream());
        double height = Math.round(image.getHeight()) / (image.getWidth() / (double) MAX_WEIGHT_AND_WIGHT_PHOTO);
    }

}
