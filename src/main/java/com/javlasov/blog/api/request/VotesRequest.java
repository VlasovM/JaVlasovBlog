package com.javlasov.blog.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VotesRequest {

    @JsonProperty("post_id")
    private int postId;

}
