package com.javlasov.blog.api.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RestoreRequest {

    String email;

}
