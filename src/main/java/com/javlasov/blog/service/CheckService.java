package com.javlasov.blog.service;

import com.javlasov.blog.api.response.CheckResponse;
import org.springframework.stereotype.Service;

@Service
public class CheckService {

    public CheckResponse getAuthCheck() {
        CheckResponse checkResponse = new CheckResponse();
        return checkResponse;
    }
}
