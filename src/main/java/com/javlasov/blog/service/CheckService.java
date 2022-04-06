package com.javlasov.blog.service;

import com.javlasov.blog.api.response.CheckResponse;
import org.springframework.stereotype.Service;

@Service
public class CheckService {

    CheckResponse checkResponse;

    public CheckResponse checkUser() {
        checkResponse = new CheckResponse();
        if (checkResponse.isResult()) {
            //TODO: Make verification of authorized users
        }
        return checkResponse;
    }
}
