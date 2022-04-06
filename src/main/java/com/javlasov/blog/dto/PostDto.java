package com.javlasov.blog.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PostDto {

    private int id;

    private long timestamp;

    private UserDto user;

    private String title;

    private String announce;

    private int likeCount;

    private int dislikeCount;

    private int commentCount;

    private int viewCount;
}
