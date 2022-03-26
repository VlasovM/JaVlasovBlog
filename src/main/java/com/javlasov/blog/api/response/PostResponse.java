package com.javlasov.blog.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.javlasov.blog.model.Post;
import lombok.Data;

import java.util.List;

@Data
public class PostResponse {

    private int count;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Post> posts;
}
