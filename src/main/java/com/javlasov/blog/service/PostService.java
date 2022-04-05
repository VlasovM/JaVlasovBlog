package com.javlasov.blog.service;

import com.javlasov.blog.api.response.PostResponse;
import com.javlasov.blog.dto.PostDto;
import com.javlasov.blog.entity.Post;
import com.javlasov.blog.entity.PostComments;
import com.javlasov.blog.entity.PostVotes;
import com.javlasov.blog.mappers.DtoMapper;
import com.javlasov.blog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final DtoMapper dtoMapper;
    private List<Post> allPostsList;
    private List<PostDto> allPostsDtoList;

    public PostResponse postResponse(String mode, int offset, int limit) {
        PostResponse postResponse = new PostResponse();
        allPostsList = postRepository.findAll();
        allPostsDtoList = dtoMapper.PostListToPostDtoList(allPostsList);
        setPostDtoVotesCount();
        setPostCommentsCount();
        setPostAnnounce();
        setPostTimestamp();
        sortCollection(mode);
        allPostsDtoList = getCollectionsByOffsetLimit(offset, limit);
        postResponse.setPostsDto(allPostsDtoList);
        postResponse.setCount(allPostsDtoList.size());
        return postResponse;
    }

    private void setPostDtoVotesCount() {
        for (int i = 0; i < allPostsList.size(); i++) {
            Post post = allPostsList.get(i);
            List<PostVotes> postVotesList = post.getPostVotes();
            PostDto postDto = allPostsDtoList.get(i);
            int like = 0;
            int dislike = 0;
            for (PostVotes postVotes : postVotesList) {
                if (postVotes.getValue() == 1) {
                    like++;
                } else if (postVotes.getValue() == -1) {
                    dislike++;
                }
            }
            postDto.setLikeCount(like);
            postDto.setDislikeCount(dislike);
        }
    }

    private void setPostCommentsCount() {
        for (int i = 0; i < allPostsList.size(); i++) {
            Post post = allPostsList.get(i);
            PostDto postDto = allPostsDtoList.get(i);
            List<PostComments> postCommentsList = post.getPostComments();
            postDto.setCommentCount(postCommentsList.size());
        }
    }

    private void setPostAnnounce() {
        int limit = 150; //Number of characters for the announcement
        for (int i = 0; i < allPostsList.size(); i++) {
            Post post = allPostsList.get(i);
            PostDto postDto = allPostsDtoList.get(i);
            String textPost = post.getText();
            if (textPost.length() > 150) {
                textPost = textPost.substring(0, limit) + "...";
            }
            postDto.setAnnounce(textPost);
        }
    }

    private void setPostTimestamp() {
        for (int i = 0; i < allPostsList.size(); i++) {
            Post post = allPostsList.get(i);
            PostDto postDto = allPostsDtoList.get(i);
            long secondsAfterCreatePost = (System.currentTimeMillis() - post.getTime().getTime()) / 1000;
            postDto.setTimestamp(secondsAfterCreatePost);
        }
    }

    private void sortCollection(String mode) {
        if (mode != null) {
            if (mode.startsWith("recent")) {
                PostComparatorByRecent comparator = new PostComparatorByRecent();
                allPostsDtoList.sort(comparator);
            }
            if (mode.startsWith("popular")) {
                PostComparatorByPopular comparator = new PostComparatorByPopular();
                allPostsDtoList.sort(comparator);
            }
            if (mode.startsWith("best")) {
                PostComparatorByBest comparator = new PostComparatorByBest();
                allPostsDtoList.sort(comparator);
            }
            if (mode.startsWith("early")) {
                PostComparatorByEarly comparator = new PostComparatorByEarly();
                allPostsDtoList.sort(comparator);
            }
        }
    }

    private List<PostDto> getCollectionsByOffsetLimit(int offset, int limit) {
        List<PostDto> postDtoList;
        if (offset + limit > allPostsDtoList.size()) {
            postDtoList = allPostsDtoList.subList(offset, allPostsDtoList.size());
        } else {
            if (offset == 0) {
                postDtoList = allPostsDtoList.subList(offset, limit);
            } else {
                int rightBorder = offset + limit;
                postDtoList = allPostsDtoList.subList(offset, rightBorder);
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

