package com.javlasov.blog.api.response;

import com.javlasov.blog.dto.TagDto;
import lombok.Data;

import java.util.List;

@Data
public class TagResponse {

    private List<TagDto> tags;
}
