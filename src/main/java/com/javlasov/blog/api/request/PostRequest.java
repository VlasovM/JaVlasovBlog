package com.javlasov.blog.api.request;

import lombok.Data;

import java.util.HashSet;

@Data
public class PostRequest {

    private long timestamp;

    private short active;

    private String title;

    private HashSet<String> tags;

    private String text;

}
