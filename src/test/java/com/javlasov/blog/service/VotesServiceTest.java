package com.javlasov.blog.service;

import com.javlasov.blog.api.response.StatusResponse;
import com.javlasov.blog.model.Post;
import com.javlasov.blog.model.PostVotes;
import com.javlasov.blog.model.User;
import com.javlasov.blog.repository.PostRepository;
import com.javlasov.blog.repository.PostVotesRepository;
import com.javlasov.blog.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

class VotesServiceTest {

    UserRepository mockUserRepo = Mockito.mock(UserRepository.class);
    PostRepository mockPostRepo = Mockito.mock(PostRepository.class);
    PostVotesRepository mockPostVotesRepo = Mockito.mock(PostVotesRepository.class);
    VotesService underTestService = new VotesService(mockPostVotesRepo, mockUserRepo, mockPostRepo);

    @Test
    @DisplayName("Set like. PostVotes is empty.")
    void setLikeWithEmptyPostVotesTest() {
        StatusResponse expectedResponse = new StatusResponse();
        expectedResponse.setResult(true);

        User user = getUser();
        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Post post = getPost();
        when(mockPostRepo.findById(post.getId())).thenReturn(Optional.of(post));

        when(mockPostVotesRepo.findByUserIdAndPostId(user.getId(), post.getId())).thenReturn(Optional.empty());

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getEmail());

        StatusResponse actualResponse = underTestService.setLike(post.getId());

        assertEquals(expectedResponse, actualResponse);

    }

    @Test
    @DisplayName("Replace dislike with like.")
    void setLikeWithPostVotesIsDislikeTest() {
        StatusResponse expectedResponse = new StatusResponse();
        expectedResponse.setResult(true);

        User user = getUser();
        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Post post = getPost();
        when(mockPostRepo.findById(post.getId())).thenReturn(Optional.of(post));

        int dislike = -1;

        PostVotes postVotes = new PostVotes();
        postVotes.setValue(dislike);
        postVotes.setPostId(post.getId());
        postVotes.setUserId(user.getId());

        when(mockPostVotesRepo.findByUserIdAndPostId(user.getId(), post.getId())).thenReturn(Optional.of(postVotes));

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getEmail());

        StatusResponse actualResponse = underTestService.setLike(post.getId());

        assertEquals(expectedResponse, actualResponse);
        assertNotEquals(dislike, postVotes.getValue());

    }

    @Test
    @DisplayName("Already set like in post.")
    void likeAlreadySetInPostTest() {
        StatusResponse expectedResponse = new StatusResponse();

        User user = getUser();
        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Post post = getPost();
        when(mockPostRepo.findById(post.getId())).thenReturn(Optional.of(post));

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getEmail());

        int like = 1;

        PostVotes postVotes = new PostVotes();
        postVotes.setUserId(user.getId());
        postVotes.setPostId(post.getId());
        postVotes.setValue(like);
        when(mockPostVotesRepo.findByUserIdAndPostId(post.getId(), user.getId())).thenReturn(Optional.of(postVotes));

        StatusResponse actualResponse = underTestService.setLike(post.getId());

        assertEquals(expectedResponse, actualResponse);

    }

    @Test
    @DisplayName("Set dislike. PostVotes is empty.")
    void setDislikeWithEmptyPostVotesTest() {
        StatusResponse expectedResponse = new StatusResponse();
        expectedResponse.setResult(true);

        User user = getUser();
        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Post post = getPost();
        when(mockPostRepo.findById(post.getId())).thenReturn(Optional.of(post));

        when(mockPostVotesRepo.findByUserIdAndPostId(user.getId(), post.getId())).thenReturn(Optional.empty());

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getEmail());

        StatusResponse actualResponse = underTestService.setDislike(post.getId());

        assertEquals(expectedResponse, actualResponse);

    }

    @Test
    @DisplayName("Replace like with dislike.")
    void setDislikeWithPostVotesIsLikeTest() {
        StatusResponse expectedResponse = new StatusResponse();
        expectedResponse.setResult(true);

        User user = getUser();
        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Post post = getPost();
        when(mockPostRepo.findById(post.getId())).thenReturn(Optional.of(post));

        int like = 1;

        PostVotes postVotes = new PostVotes();
        postVotes.setValue(like);
        postVotes.setPostId(post.getId());
        postVotes.setUserId(user.getId());

        when(mockPostVotesRepo.findByUserIdAndPostId(user.getId(), post.getId())).thenReturn(Optional.of(postVotes));

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getEmail());

        StatusResponse actualResponse = underTestService.setDislike(post.getId());

        assertEquals(expectedResponse, actualResponse);
        assertNotEquals(like, postVotes.getValue());

    }

    @Test
    @DisplayName("Already set dislike in post.")
    void dislikeAlreadySetInPostTest() {
        StatusResponse expectedResponse = new StatusResponse();

        User user = getUser();
        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Post post = getPost();
        when(mockPostRepo.findById(post.getId())).thenReturn(Optional.of(post));

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getEmail());

        int dislike = -1;

        PostVotes postVotes = new PostVotes();
        postVotes.setUserId(user.getId());
        postVotes.setPostId(post.getId());
        postVotes.setValue(dislike);
        when(mockPostVotesRepo.findByUserIdAndPostId(post.getId(), user.getId())).thenReturn(Optional.of(postVotes));

        StatusResponse actualResponse = underTestService.setDislike(post.getId());

        assertEquals(expectedResponse, actualResponse);

    }


    private User getUser() {
        User user = new User();
        user.setModerator(0);
        user.setEmail("test@mail.ru");
        user.setName("Maxim");
        user.setId(0);

        return user;
    }

    private Post getPost() {
        Post post = new Post();
        post.setId(0);
        return post;
    }
}