package com.javlasov.blog.dto;

import com.javlasov.blog.model.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@RequiredArgsConstructor
public class PostDto {

    private int id;

    private long timestamp;

    private UserPostsDto user;

    private String title;

    private String announce;

    private int likeCount;

    private int dislikeCount;

    private int commentCount;

    private int viewCount;

}
