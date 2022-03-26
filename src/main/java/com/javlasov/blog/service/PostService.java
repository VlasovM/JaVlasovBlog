package com.javlasov.blog.service;

import com.javlasov.blog.api.response.PostResponse;
import com.javlasov.blog.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private PostResponse postResponse;

    @Autowired
    private EntityManager entityManager;

    public PostResponse getPosts(int limit) {
        List<Post> allPosts = new


        return postResponse;
    }

    private ArrayList<Post> findAllPosts() {
        Query query = entityManager.createQuery("from Post");

    }
}
