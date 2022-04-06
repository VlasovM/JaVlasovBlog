package com.javlasov.blog.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.javlasov.blog.entity.User;
import lombok.Data;

@Data
@JsonPropertyOrder({"result", "User"})
public class CheckResponse {

    @JsonProperty("result")
    private boolean isResult;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private User user;
}
