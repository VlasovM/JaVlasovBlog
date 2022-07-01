package com.javlasov.blog.api.response;

import lombok.Data;

@Data
public class PersonalStatisticsResponse {

    int postCount;

    int likeCount;

    int dislikeCount;

    int viewCount;

    long firstPublication;

}
