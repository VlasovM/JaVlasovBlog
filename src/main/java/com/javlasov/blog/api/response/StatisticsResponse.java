package com.javlasov.blog.api.response;

import lombok.Data;

@Data
public class StatisticsResponse {

    int postsCount;

    int likesCount;

    int dislikesCount;

    int viewsCount;

    long firstPublication;

}
