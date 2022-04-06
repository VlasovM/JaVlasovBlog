package com.javlasov.blog.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TagDto {

    private String name;
    private double weight;

}
