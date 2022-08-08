package com.javlasov.blog.service;

import com.javlasov.blog.aop.exceptions.UnauthorizedExceptions;
import com.javlasov.blog.api.response.StatisticsResponse;
import com.javlasov.blog.model.GlobalSettings;
import com.javlasov.blog.model.Post;
import com.javlasov.blog.model.PostVotes;
import com.javlasov.blog.model.User;
import com.javlasov.blog.model.enums.ModerationStatus;
import com.javlasov.blog.repository.GlobalSettingRepository;
import com.javlasov.blog.repository.PostRepository;
import com.javlasov.blog.repository.PostVotesRepository;
import com.javlasov.blog.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class StatisticServiceTest {

    UserRepository mockUserRepo = Mockito.mock(UserRepository.class);
    PostRepository mockPostRepo = Mockito.mock(PostRepository.class);
    PostVotesRepository mockPostVotesRepo = Mockito.mock(PostVotesRepository.class);
    GlobalSettingRepository mockGlobalSettingRepo = Mockito.mock(GlobalSettingRepository.class);
    StatisticService underTestService = new StatisticService(
            mockPostVotesRepo, mockUserRepo, mockPostRepo, mockGlobalSettingRepo);

    @Test
    @DisplayName("Get statistics auth user")
    void getMyStatisticsTest() {
        StatisticsResponse expectedResponse = new StatisticsResponse();
        expectedResponse.setLikesCount(2);
        expectedResponse.setDislikesCount(1);
        expectedResponse.setFirstPublication(System.currentTimeMillis() / 1000L);
        expectedResponse.setViewsCount(60);
        expectedResponse.setPostsCount(3);

        User user = getUser();
        List<PostVotes> postVotesList = getPostVotesList();
        List<Post> postsList = getPostsList();

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getEmail());

        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(mockPostVotesRepo.findByUserId(user.getId())).thenReturn(Optional.of(postVotesList));
        when(mockPostRepo.findPostsByUserIdAndAcceptedStatus(user.getId())).thenReturn(postsList);

        StatisticsResponse actualResponse = underTestService.getMyStatistics();

        assertEquals(expectedResponse, actualResponse);

    }

    @Test
    @DisplayName("Get all statistics with `YES` value from Global Settings")
    void getAllStatisticsTest() {
        StatisticsResponse expectedResponse = new StatisticsResponse();
        expectedResponse.setLikesCount(2);
        expectedResponse.setDislikesCount(1);
        expectedResponse.setFirstPublication(System.currentTimeMillis() / 1000L);
        expectedResponse.setViewsCount(60);
        expectedResponse.setPostsCount(3);

        User user = getUser();

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getEmail());

        GlobalSettings statisticsIsPublic = new GlobalSettings();
        statisticsIsPublic.setValue("YES");
        statisticsIsPublic.setCode("STATISTICS_IS_PUBLIC");

        List<Post> postsList = getPostsList();
        List<PostVotes> postVotesList = getPostVotesList();

        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(mockGlobalSettingRepo.findByCode(statisticsIsPublic.getCode())).thenReturn(statisticsIsPublic);
        when(mockPostRepo.findAll()).thenReturn(postsList);
        when(mockPostVotesRepo.findAll()).thenReturn(postVotesList);

        StatisticsResponse actualResponse = underTestService.getAllStatistics();

        assertEquals(expectedResponse, actualResponse);

    }

    @Test
    @DisplayName("Value is NO from Global Setting. User is not a moderator.")
    void shouldGetUnauthorizedExceptionTest() {
        User user = getUser();

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getEmail());

        GlobalSettings statisticsIsPublic = new GlobalSettings();
        statisticsIsPublic.setValue("NO");
        statisticsIsPublic.setCode("STATISTICS_IS_PUBLIC");

        when(mockGlobalSettingRepo.findByCode(statisticsIsPublic.getCode())).thenReturn(statisticsIsPublic);

        assertThrows(UnauthorizedExceptions.class, () -> underTestService.getAllStatistics());

    }

    @Test
    @DisplayName("Get all statistics with value `NO`. User is a moderator.")
    void shouldGetAllStatisticsUserIsModerator() {
        StatisticsResponse expectedResponse = new StatisticsResponse();
        expectedResponse.setLikesCount(2);
        expectedResponse.setDislikesCount(1);
        expectedResponse.setFirstPublication(System.currentTimeMillis() / 1000L);
        expectedResponse.setViewsCount(60);
        expectedResponse.setPostsCount(3);

        User user = getUser();
        user.setModerator(1);

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getEmail());

        GlobalSettings statisticsIsPublic = new GlobalSettings();
        statisticsIsPublic.setValue("YES");
        statisticsIsPublic.setCode("STATISTICS_IS_PUBLIC");

        List<Post> postsList = getPostsList();
        List<PostVotes> postVotesList = getPostVotesList();

        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(mockGlobalSettingRepo.findByCode(statisticsIsPublic.getCode())).thenReturn(statisticsIsPublic);
        when(mockPostRepo.findAll()).thenReturn(postsList);
        when(mockPostVotesRepo.findAll()).thenReturn(postVotesList);

        StatisticsResponse actualResponse = underTestService.getAllStatistics();

        assertEquals(expectedResponse, actualResponse);

    }

    private User getUser() {
        User user = new User();
        user.setName("Maxim");
        user.setEmail("test@mail.ru");
        user.setModerator(0);
        user.setId(3);

        return user;
    }

    private List<PostVotes> getPostVotesList() {
        List<PostVotes> postVotesList = new ArrayList<>();

        User user = getUser();
        List<Post> posts = getPostsList();

        PostVotes postVotesOne = new PostVotes();
        PostVotes postVotesTwo = new PostVotes();
        PostVotes postVotesThree = new PostVotes();

        postVotesOne.setUserId(user.getId());
        postVotesTwo.setUserId(user.getId());
        postVotesThree.setUserId(user.getId());

        postVotesOne.setPostId(posts.get(0).getId());
        postVotesOne.setPostId(posts.get(1).getId());
        postVotesOne.setPostId(posts.get(2).getId());

        postVotesOne.setValue(1);
        postVotesTwo.setValue(1);
        postVotesThree.setValue(-1);

        postVotesList.add(postVotesOne);
        postVotesList.add(postVotesTwo);
        postVotesList.add(postVotesThree);

        return postVotesList;
    }

    private List<Post> getPostsList() {
        List<Post> postsList = new ArrayList<>();
        User user = getUser();

        Post postOne = new Post();
        Post postTwo = new Post();
        Post postThree = new Post();

        postOne.setId(0);
        postTwo.setId(1);
        postThree.setId(2);

        postOne.setModerationStatus(ModerationStatus.ACCEPTED);
        postTwo.setModerationStatus(ModerationStatus.ACCEPTED);
        postThree.setModerationStatus(ModerationStatus.ACCEPTED);

        postOne.setUser(user);
        postTwo.setUser(user);
        postThree.setUser(user);

        postOne.setViewCount(10);
        postTwo.setViewCount(20);
        postThree.setViewCount(30);

        postOne.setTime(LocalDateTime.now());
        postTwo.setTime(LocalDateTime.now().minusHours(1));
        postThree.setTime(LocalDateTime.now().minusHours(2));

        postsList.add(postOne);
        postsList.add(postTwo);
        postsList.add(postThree);

        return postsList;
    }

}