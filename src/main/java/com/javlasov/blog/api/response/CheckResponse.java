package com.javlasov.blog.api.response;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class CheckResponse {
    private boolean result;
}
