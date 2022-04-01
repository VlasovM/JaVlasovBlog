package com.javlasov.blog.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.javlasov.blog.dto.PostDto;
import lombok.Data;

import java.util.List;

@Data
public class PostResponse {

    private int count;

    @JsonProperty("posts")
    private List<PostDto> postsDto;
}
