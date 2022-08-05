package com.javlasov.blog.service;

import com.javlasov.blog.api.response.StatisticsResponse;
import com.javlasov.blog.aop.exceptions.UnauthorizedExceptions;
import com.javlasov.blog.model.GlobalSettings;
import com.javlasov.blog.model.Post;
import com.javlasov.blog.model.PostVotes;
import com.javlasov.blog.model.User;
import com.javlasov.blog.repository.GlobalSettingRepository;
import com.javlasov.blog.repository.PostRepository;
import com.javlasov.blog.repository.PostVotesRepository;
import com.javlasov.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final PostVotesRepository postVotesRepository;

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final GlobalSettingRepository globalSettingRepository;

    public StatisticsResponse getMyStatistics() {
        StatisticsResponse response = new StatisticsResponse();
        String emailUser = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(emailUser).orElseThrow();
        List<PostVotes> postVotesList = postVotesRepository.findByUserId(user.getId()).orElseThrow();
        int like = 0;
        int dislike = 0;

        for (PostVotes postVotes : postVotesList) {
            if (postVotes.getValue() == 1) {
                like++;
            } else {
                dislike++;
            }
        }

        response.setPostsCount(getCountPosts(user.getId()));
        response.setLikesCount(like);
        response.setDislikesCount(dislike);
        response.setViewsCount(getViewsCount(user.getId()));
        response.setFirstPublication(getTimeFirstPublication(user.getId()));

        return response;
    }

    public StatisticsResponse getAllStatistics() {
        StatisticsResponse statisticsResponse = new StatisticsResponse();
        String emailUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByEmail(emailUser);

        GlobalSettings statisticsIsPublic = globalSettingRepository.findByCode("STATISTICS_IS_PUBLIC");

        if (statisticsIsPublic.getValue().equals("NO")) {
            if (user.isEmpty() || user.orElseThrow().getModerator() == 0) {
                throw new UnauthorizedExceptions();
            }
        }

        List<Post> allPosts = postRepository.findAll();
        statisticsResponse.setPostsCount(allPosts.size());

        statisticsResponse.setLikesCount(getLikesAllPosts());
        statisticsResponse.setDislikesCount(getDislikeAllPosts());
        statisticsResponse.setViewsCount(getViewsCountAllPosts(allPosts));
        statisticsResponse.setFirstPublication(getTimeFirstPublicationAllPosts(allPosts));

        return statisticsResponse;
    }

    private int getViewsCountAllPosts(List<Post> allPosts) {
        int viewsCount = 0;
        for (Post post : allPosts) {
            viewsCount = viewsCount + post.getViewCount();
        }
        return viewsCount;
    }

    private long getTimeFirstPublicationAllPosts(List<Post> allPosts) {
        long timeUTC = 0;
        for (Post post : allPosts) {
            Duration duration = Duration.between(post.getTime(), LocalDateTime.now());
            long secondsAfterCreatePost = (System.currentTimeMillis() / 1000L) - duration.getSeconds();
            if (secondsAfterCreatePost > timeUTC) {
                timeUTC = secondsAfterCreatePost;
            }
        }
        return timeUTC;
    }

    private int getLikesAllPosts() {
        List<PostVotes> postVotesList = postVotesRepository.findAll();
        int likes = 0;
        for (PostVotes postVotes : postVotesList) {
            if (postVotes.getValue() == 1) {
                likes++;
            }
        }
        return likes;
    }

    private int getDislikeAllPosts() {
        List<PostVotes> postVotesList = postVotesRepository.findAll();
        int dislike = 0;
        for (PostVotes postVotes : postVotesList) {
            if (postVotes.getValue() == -1) {
                dislike++;
            }
        }
        return dislike;
    }

    private int getCountPosts(int userId) {
        return postRepository.findPostsByUserIdAndAcceptedStatus(userId).size();
    }

    private int getViewsCount(int userId) {
        int viewsCount = 0;
        List<Post> posts = postRepository.findPostsByUserIdAndAcceptedStatus(userId);
        for (Post post : posts) {
            viewsCount = viewsCount + post.getViewCount();
        }
        return viewsCount;
    }

    private long getTimeFirstPublication(int userId) {
        long timeUTC = 0;
        List<Post> posts = postRepository.findPostsByUserIdAndAcceptedStatus(userId);
        for (Post post : posts) {
            Duration duration = Duration.between(post.getTime(), LocalDateTime.now());
            long secondsAfterCreatePost = (System.currentTimeMillis() / 1000L) - duration.getSeconds();
            if (secondsAfterCreatePost > timeUTC) {
                timeUTC = secondsAfterCreatePost;
            }
        }
        return timeUTC;
    }

}
