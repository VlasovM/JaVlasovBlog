package com.javlasov.blog.service;

import com.javlasov.blog.api.response.PostResponse;
import com.javlasov.blog.dto.PostDto;
import com.javlasov.blog.entity.Post;
import com.javlasov.blog.entity.PostComments;
import com.javlasov.blog.entity.PostVotes;
import com.javlasov.blog.mappers.PostMapper;
import com.javlasov.blog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private List<Post> allPostsList;
    private List<PostDto> allPostsDtoList;

    public PostResponse postResponse() {
        PostResponse postResponse = new PostResponse();
        allPostsList = postRepository.findAll();
        allPostsDtoList = postMapper.toDtoList(allPostsList);
        setPostDtoVotesCount();
        setPostCommentsCount();
        setPostAnnounce();
        setPostTimestamp();

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
            for (int j = 0; j < postVotesList.size(); j++) {
                if (postVotesList.get(j).getValue() == 1) {
                    like++;
                } else if (postVotesList.get(j).getValue() == -1) {
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


}

//    public PostResponse getPostResponse() {
//        List<Post> allPosts;
//
//
//
//        return null;
//    }
//
//    private List findAllPosts() {
//        List<Post> allPosts = postRepository.findAll();
//        return null;
//    }
//
//    private List<Post> outputOfPosts(int offset, int limit) {
//        List<Post> allPosts = findAllPosts();
//        List<Post> postList;
//
//        if (offset + limit > allPosts.size()) {
//            postList = allPosts.subList(offset, allPosts.size());
//        } else {
//            if (offset == 0) {
//                postList = allPosts.subList(offset, limit);
//            } else {
//                int rightBorder = offset + limit;
//                postList = allPosts.subList(offset, rightBorder);
//            }
//        }
//        return postList;
//    }
