package com.javlasov.blog.repository;

import com.javlasov.blog.model.PostComments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<PostComments, Integer> {

    PostComments findTopByOrderById();

}
