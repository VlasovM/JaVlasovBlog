package com.javlasov.blog.api.request;

import lombok.Data;

import java.util.List;

@Data
public class PostRequest {

    private long timestamp;

    private short active;

    private String title;

    private List<String> tags;

    private String text;

}
