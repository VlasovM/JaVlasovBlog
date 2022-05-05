package com.javlasov.blog.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostCommentDto {

    private int id;

    private long timestamp;

    private String text;

    private UserPostsDto user;

}
