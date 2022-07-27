package com.javlasov.blog.api.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModerationRequest {

    @JsonProperty("post_id")
    private int postId;

    private String decision;

}
