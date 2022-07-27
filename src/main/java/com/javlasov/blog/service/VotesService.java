package com.javlasov.blog.service;

import com.javlasov.blog.api.response.StatusResponse;
import com.javlasov.blog.model.Post;
import com.javlasov.blog.model.PostVotes;
import com.javlasov.blog.model.User;
import com.javlasov.blog.repository.PostRepository;
import com.javlasov.blog.repository.PostVotesRepository;
import com.javlasov.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VotesService {

    private final PostVotesRepository postVotesRepository;

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    public StatusResponse setLike(int postId) {
        StatusResponse statusResponse = new StatusResponse();
        String emailUser = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(emailUser).orElseThrow();
        statusResponse.setResult(setLike(postId, user));
        return statusResponse;
    }

    public StatusResponse setDislike(int postId) {
        StatusResponse statusResponse = new StatusResponse();
        String emailUser = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(emailUser).orElseThrow();
        statusResponse.setResult(setDislike(postId, user));
        return statusResponse;
    }

    private boolean setLike(int postId, User user) {
        Optional<PostVotes> postVotesOptional = postVotesRepository.findByUserIdAndPostId(postId, user.getId());

        if (postVotesOptional.isEmpty()) {
            Post post = postRepository.findById(postId).orElseThrow();
            PostVotes postVotes = new PostVotes();
            postVotes.setTime(Timestamp.valueOf(LocalDateTime.now()));
            postVotes.setUserId(user.getId());
            postVotes.setPostId(post.getId());
            postVotes.setValue(1);
            postVotesRepository.save(postVotes);
            return true;
        }
        if (postVotesOptional.orElseThrow().getValue() == -1) {
            PostVotes postVotes = postVotesOptional.orElseThrow();
            postVotes.setValue(1);
            postVotesRepository.save(postVotes);
            return true;
        }
        return false;
    }

    private boolean setDislike(int postId, User user) {
        Optional<PostVotes> postVotesOptional = postVotesRepository.findByUserIdAndPostId(postId, user.getId());

        if (postVotesOptional.isEmpty()) {
            Post post = postRepository.findById(postId).orElseThrow();
            PostVotes postVotes = new PostVotes();
            postVotes.setTime(Timestamp.valueOf(LocalDateTime.now()));
            postVotes.setUserId(user.getId());
            postVotes.setPostId(post.getId());
            postVotes.setValue(-1);
            postVotesRepository.save(postVotes);
            return true;
        }
        if (postVotesOptional.orElseThrow().getValue() == 1) {
            PostVotes postVotes = postVotesOptional.orElseThrow();
            postVotes.setValue(-1);
            postVotesRepository.save(postVotes);
            return true;
        }
        return false;
    }

}

