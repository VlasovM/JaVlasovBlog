package com.javlasov.blog.service;

import com.javlasov.blog.api.response.PersonalStatisticsResponse;
import com.javlasov.blog.model.Post;
import com.javlasov.blog.model.PostVotes;
import com.javlasov.blog.model.User;
import com.javlasov.blog.repository.PostRepository;
import com.javlasov.blog.repository.PostVotesRepository;
import com.javlasov.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final PostVotesRepository postVotesRepository;

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    public PersonalStatisticsResponse getMyStatistics() {
        PersonalStatisticsResponse response = new PersonalStatisticsResponse();
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

        response.setPostCount(getCountPosts(user.getId()));
        response.setLikeCount(like);
        response.setDislikeCount(dislike);
        response.setViewCount(getViewsCount(user.getId()));

        return response;
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

    private long getTimeFirstPublication (int userId) {
        //TODO: make time publication
        long timeUTC = 0;
        List<Post> posts = postRepository.findPostsByUserIdAndAcceptedStatus(userId);
        return timeUTC;
    }
}
