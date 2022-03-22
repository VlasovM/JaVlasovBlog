package com.javlasov.blog.service;

import com.javlasov.blog.api.response.PostResponse;
import com.javlasov.blog.model.Post;
import com.javlasov.blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;
    PostResponse postResponse = new PostResponse();
    public static List<Post> postList;

    public PostResponse getPostResponse() {
        createPostsList();
        postResponse.setCount(postList.size());


        return postResponse;
    }

    public List<Post> showPosts(int limit) {

    }

    public List<Post> createPostsList() {
        postList = new ArrayList<>();
        Iterable<Post> postIterable = postRepository.findAll();
        for (Post post : postIterable) {
            if ((post.getIs_active() == 1) && (post.getModeration_status().equals("ACCEPTED")) &&
                    (post.getTime().isAfter(LocalDate.now()))) {
                postList.add(post);
            }
        }
        return postList;
    }

    /**
     * Должны выводиться только активные (поле is_active в таблице posts равно 1), утверждённые
     * модератором (поле moderation_status равно ACCEPTED) посты с датой публикации не позднее
     * текущего момента (движок должен позволять откладывать публикацию).
     */

}
