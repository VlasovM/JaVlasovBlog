package com.javlasov.blog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonPropertyOrder({"id", "timestamp", "active", "user", "title", "text", "likeCount", "dislikeCount", "viewCount", "comments", "tags"})
public class PostDtoById {

    private long timestamp;
    private int id;
    @JsonProperty("active")
    private boolean isActive;
    private String title;
    private String text;
    private int likeCount;
    private UserDtoForPosts user;
    private int dislikeCount;
    private int viewCount;
    private List<PostCommentDto> comments;
    private List<String> tags;

}
