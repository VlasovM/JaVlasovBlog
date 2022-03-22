package com.javlasov.blog.api.response;

import lombok.Data;
import com.javlasov.blog.model.Post;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Data
@Component
public class PostResponse {
    private int count;
    private ArrayList<Post> posts;
}
