package com.javlasov.blog.service;

import com.javlasov.blog.aop.exceptions.BadRequestExceptions;
import com.javlasov.blog.api.request.CommentRequest;
import com.javlasov.blog.api.response.CommentResponse;
import com.javlasov.blog.model.Post;
import com.javlasov.blog.model.PostComments;
import com.javlasov.blog.model.User;
import com.javlasov.blog.model.enums.ModerationStatus;
import com.javlasov.blog.repository.CommentRepository;
import com.javlasov.blog.repository.PostRepository;
import com.javlasov.blog.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class CommentServiceTest {

    PostRepository mockPostRepo = Mockito.mock(PostRepository.class);
    UserRepository mockUserRepo = Mockito.mock(UserRepository.class);
    CommentRepository mockCommentRepo = Mockito.mock(CommentRepository.class);
    CommentService underTestService = new CommentService(mockPostRepo, mockCommentRepo, mockUserRepo);

    @Test
    @DisplayName("Set correct comment")
    void setCorrectCommentTest() {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setPostId(1);
        commentRequest.setParentId("1");
        commentRequest.setText("Тестовый текст для комментария длинее минимального требования.");

        CommentResponse expectedResponse = new CommentResponse();
        expectedResponse.setId(0);
        ResponseEntity<CommentResponse> expected = ResponseEntity.ok(expectedResponse);

        User user = getUser();
        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Post post = getPost();
        when(mockPostRepo.findById(post.getId())).thenReturn(Optional.of(post));

        PostComments postComments = getPostComment(commentRequest.getText(), commentRequest.getParentId());
        when(mockCommentRepo.findTopByOrderById()).thenReturn(postComments);

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getEmail());

        ResponseEntity<?> actual = underTestService.setCommentToPost(
                commentRequest.getParentId(),
                commentRequest.getPostId(),
                commentRequest.getText());

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Comment text is short")
    void setShortCommentTextTest() {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setPostId(1);
        commentRequest.setParentId("1");
        commentRequest.setText(""); //minimum length is 5

        User user = getUser();
        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Post post = getPost();
        when(mockPostRepo.findById(post.getId())).thenReturn(Optional.of(post));

        PostComments postComments = getPostComment(commentRequest.getText(), commentRequest.getParentId());
        when(mockCommentRepo.findTopByOrderById()).thenReturn(postComments);

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getEmail());

        assertThrows(BadRequestExceptions.class, () -> underTestService.setCommentToPost(
                commentRequest.getParentId(),
                commentRequest.getPostId(),
                commentRequest.getText()
        ));

    }

    @Test
    @DisplayName("IfIncorrectlyAuthUserTest")
    void incorrectAuthUserTest() {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setPostId(1);
        commentRequest.setParentId("1");
        commentRequest.setText("Тестовый текст для комментария длинее минимального требования.");

        User user = getUser();
        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Post post = getPost();
        when(mockPostRepo.findById(post.getId())).thenReturn(Optional.of(post));

        PostComments postComments = getPostComment(commentRequest.getText(), commentRequest.getParentId());
        when(mockCommentRepo.findTopByOrderById()).thenReturn(postComments);

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("incorrectEmail@mail.ru");

        assertThrows(NoSuchElementException.class, () -> underTestService.setCommentToPost(
                commentRequest.getParentId(),
                commentRequest.getPostId(),
                commentRequest.getText()));

    }

    private User getUser() {
        User user = new User();
        user.setName("Maxim");
        user.setEmail("test@mail.ru");
        user.setModerator(0);
        return user;
    }

    private Post getPost() {
        Post post = new Post();
        User user = getUser();
        post.setUser(user);
        post.setId(1);
        post.setUser(user);
        post.setActive((short) 1);
        post.setTime(LocalDateTime.of(2022, 7, 19, 20, 0, 0));
        post.setModerationStatus(ModerationStatus.ACCEPTED);
        return post;
    }

    private PostComments getPostComment(String text, String parentId) {
        Post post = getPost();
        User user = getUser();
        PostComments postComments = new PostComments();
        postComments.setPostId(post.getId());
        postComments.setTime(LocalDateTime.now());
        postComments.setText(text);
        postComments.setUserId(user.getId());
        postComments.setParentId(Integer.parseInt(parentId));

        return postComments;
    }

}