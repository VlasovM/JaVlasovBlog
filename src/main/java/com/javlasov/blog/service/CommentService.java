package com.javlasov.blog.service;

import com.javlasov.blog.aop.exceptions.BadRequestExceptions;
import com.javlasov.blog.api.response.CommentResponse;
import com.javlasov.blog.model.Post;
import com.javlasov.blog.model.PostComments;
import com.javlasov.blog.model.User;
import com.javlasov.blog.repository.CommentRepository;
import com.javlasov.blog.repository.PostRepository;
import com.javlasov.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    public ResponseEntity<CommentResponse> setCommentToPost(String parentId, int postId, String text) {
        if (text.length() < 5) {
            Map<String, String> errors = new HashMap<>();
            errors.put("text", "Текст комментария должен быть не менее 5 символов");
            throw new BadRequestExceptions(errors);
        }

        CommentResponse commentResponse = new CommentResponse();
        int commentId = setComment(parentId, postId, text);
        commentResponse.setId(commentId);
        return ResponseEntity.ok(commentResponse);
    }

    private int setComment(String parentIdString, int postId, String text) {
        Optional<Post> post = postRepository.findById(postId);
        String emailUser = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(emailUser).orElseThrow();
        PostComments postComments = new PostComments();
        postComments.setPostId(post.orElseThrow().getId());
        postComments.setTime(LocalDateTime.now());
        postComments.setUserId(user.getId());
        postComments.setText(text);
        if (parentIdString == null) {
            postComments.setParentId(null);
            commentRepository.save(postComments);
            return commentRepository.findTopByOrderById().getId();
        }
        postComments.setParentId(Integer.parseInt(parentIdString));
        commentRepository.save(postComments);
        return commentRepository.findTopByOrderById().getId();
    }

}
