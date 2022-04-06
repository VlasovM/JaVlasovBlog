package com.javlasov.blog.service;

import com.javlasov.blog.api.response.PostResponse;
import com.javlasov.blog.dto.PostDto;
import com.javlasov.blog.entity.Post;
import com.javlasov.blog.entity.PostVotes;
import com.javlasov.blog.mappers.DtoMapper;
import com.javlasov.blog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final DtoMapper dtoMapper;

    public PostResponse postResponse(String mode, int offset, int limit) {
        PostResponse postResponse = new PostResponse();
        List<Post> allPostsList = postRepository.findAll();
        List<PostDto> postDtoList = preparePost(allPostsList);
        sortCollection(mode, postDtoList);
        postDtoList = getCollectionsByOffsetLimit(offset, limit, postDtoList);
        postResponse.setPostsDto(postDtoList);
        postResponse.setCount(allPostsList.size());
        return postResponse;
    }

    private List<PostDto> preparePost(List<Post> posts) {
        List<PostDto> result = new ArrayList<>();
        for (Post post : posts) {
            PostDto postDto = dtoMapper.PostToPostDto(post);
            setPostDtoVotesCount(post, postDto);
            setPostCommentsCount(post, postDto);
            setPostAnnounce(post, postDto);
            setPostTimestamp(post, postDto);
            result.add(postDto);
        }
        return result;
    }

    private void setPostDtoVotesCount(Post post, PostDto postDto) {
        List<PostVotes> postVotesList = post.getPostVotes();
        int like = 0;
        int dislike = 0;
        for (PostVotes postVotes : postVotesList) {
            if (postVotes.getValue() == 1) {
                like++;
            }
            dislike++;
        }
        postDto.setLikeCount(like);
        postDto.setDislikeCount(dislike);
    }

    private void setPostCommentsCount(Post post, PostDto postDto) {
        postDto.setCommentCount(post.getPostComments().size());
    }

    private void setPostAnnounce(Post post, PostDto postDto) {
        int limit = 150; //Number of characters for the announcement
        String text = (post.getText().length() > limit) ? post.getText() + "..." : post.getText();
        postDto.setAnnounce(text);
    }

    private void setPostTimestamp(Post post, PostDto postDto) {
        Duration duration = Duration.between(post.getTime(), LocalDateTime.now());
        long secondsAfterCreatePost = duration.getSeconds();
        postDto.setTimestamp(secondsAfterCreatePost);
    }

    private void sortCollection(String mode, List<PostDto> postDtoList) {
        if (mode != null) {
            if (mode.startsWith("recent")) {
                PostComparatorByRecent comparator = new PostComparatorByRecent();
                postDtoList.sort(comparator);
            }
            if (mode.startsWith("popular")) {
                PostComparatorByPopular comparator = new PostComparatorByPopular();
                postDtoList.sort(comparator);
            }
            if (mode.startsWith("best")) {
                PostComparatorByBest comparator = new PostComparatorByBest();
                postDtoList.sort(comparator);
            }
            if (mode.startsWith("early")) {
                PostComparatorByEarly comparator = new PostComparatorByEarly();
                postDtoList.sort(comparator);
            }
        }
    }

    private List<PostDto> getCollectionsByOffsetLimit(int offset, int limit, List<PostDto> postDtoList) {

        if (offset + limit > postDtoList.size()) {
            postDtoList = postDtoList.subList(offset, postDtoList.size());
        } else {
            if (offset == 0) {
                postDtoList = postDtoList.subList(offset, limit);
            } else {
                int rightBorder = offset + limit;
                postDtoList = postDtoList.subList(offset, rightBorder);
            }
        }
        return postDtoList;
    }
}

class PostComparatorByRecent implements Comparator<PostDto> {

    @Override
    public int compare(PostDto o1, PostDto o2) {
        return (int) (o1.getTimestamp() - o2.getTimestamp());
    }

}

class PostComparatorByPopular implements Comparator<PostDto> {

    @Override
    public int compare(PostDto o1, PostDto o2) {
        return o2.getCommentCount() - o1.getCommentCount();
    }
}

class PostComparatorByBest implements Comparator<PostDto> {

    @Override
    public int compare(PostDto o1, PostDto o2) {
        return o2.getLikeCount() - o1.getLikeCount();
    }
}

class PostComparatorByEarly implements Comparator<PostDto> {

    @Override
    public int compare(PostDto o1, PostDto o2) {
        return (int) (o2.getTimestamp() - o1.getTimestamp());
    }
}

