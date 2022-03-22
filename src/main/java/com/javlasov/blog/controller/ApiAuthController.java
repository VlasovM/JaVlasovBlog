package com.javlasov.blog.controller;

import com.javlasov.blog.api.response.CheckResponse;
import com.javlasov.blog.service.PostService;
import com.javlasov.blog.service.CheckService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private CheckService checkResponse;
    private PostService postService;

    public ApiAuthController(CheckService checkResponse, PostService postService) {
        this.checkResponse = checkResponse;
        this.postService = postService;
    }

    @GetMapping("/check")
    private ResponseEntity<CheckResponse> checkAuth() {
        //check Map identifier;
        return new ResponseEntity<>(checkResponse.getAuthCheck(), HttpStatus.OK);
    }
}
