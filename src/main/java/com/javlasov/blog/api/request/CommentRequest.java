package com.javlasov.blog.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CommentRequest {

    @JsonProperty(value = "parent_id")
    String parentId;

    @JsonProperty(value = "post_id")
    int postId;

    String text;

}
