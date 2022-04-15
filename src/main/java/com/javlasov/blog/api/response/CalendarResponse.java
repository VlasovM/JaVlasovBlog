package com.javlasov.blog.api.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
public class CalendarResponse {

    private Set<Integer> years;
    private Map<String, Long> posts;

}
