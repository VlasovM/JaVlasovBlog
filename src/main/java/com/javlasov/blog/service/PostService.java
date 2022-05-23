package com.javlasov.blog.service;

import com.javlasov.blog.api.response.PostResponse;
import com.javlasov.blog.api.response.RegisterResponse;
import com.javlasov.blog.constants.CommonConstants;
import com.javlasov.blog.dto.PostCommentDto;
import com.javlasov.blog.dto.PostDto;
import com.javlasov.blog.dto.PostDtoById;
import com.javlasov.blog.dto.UserPostsDto;
import com.javlasov.blog.mappers.DtoMapper;
import com.javlasov.blog.model.*;
import com.javlasov.blog.model.enums.ModerationStatus;
import com.javlasov.blog.repository.PostRepository;
import com.javlasov.blog.repository.Tag2PostRepository;
import com.javlasov.blog.repository.TagRepository;
import com.javlasov.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final DtoMapper dtoMapper;

    private final TagRepository tagRepository;

    private final UserRepository userRepository;

    private final Tag2PostRepository tag2PostRepository;

    public PostResponse getAllPosts(String mode, int offset, int limit) {
        PostResponse postResponse = new PostResponse();
        List<Post> allPostsList = postRepository.findAll();
        List<PostDto> postDtoList = preparePost(allPostsList);
        sortCollection(mode, postDtoList);
        postDtoList = getCollectionsByOffsetLimit(offset, limit, postDtoList);
        postResponse.setPostsDto(postDtoList);
        postResponse.setCount(postDtoList.size());
        return postResponse;
    }

    public PostResponse postSearch(String query, int offset, int limit) {
        PostResponse postResponse = new PostResponse();
        List<Post> allPostsWithQuery = findPostWithQuery(query);
        List<PostDto> postDtoList = preparePost(allPostsWithQuery);
        postDtoList = getCollectionsByOffsetLimit(offset, limit, postDtoList);
        postResponse.setPostsDto(postDtoList);
        postResponse.setCount(postDtoList.size());
        return postResponse;
    }

    public PostResponse getPostByDate(String date, int offset, int limit) {
        PostResponse postResponse = new PostResponse();
        List<Post> allPosts = postRepository.findAll();
        List<Post> postsByDate = findPostByDate(allPosts, date);
        List<PostDto> postDtoList = preparePost(postsByDate);
        postDtoList = getCollectionsByOffsetLimit(offset, limit, postDtoList);
        postResponse.setCount(postDtoList.size());
        postResponse.setPostsDto(postDtoList);
        return postResponse;
    }

    public PostResponse getPostByTag(String tag, int offset, int limit) {
        PostResponse postResponse = new PostResponse();
        List<Post> posts = findPostByTag(tag);
        List<PostDto> postDtoList = preparePost(posts);
        postDtoList = getCollectionsByOffsetLimit(offset, limit, postDtoList);
        postResponse.setPostsDto(postDtoList);
        postResponse.setCount(postDtoList.size());
        return postResponse;
    }

    public PostDtoById getPostById(int id) {
        Post post = postRepository.getById(id);
        return preparePost(post);
    }

    public PostResponse getMyPosts(String status, int offset, int limit) {
        PostResponse postResponse = new PostResponse();
        String emailCurrentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(emailCurrentUser).orElseThrow(() -> new UsernameNotFoundException(emailCurrentUser));
        List<Post> posts = postRepository.findPostsByUserId(user.getId());
        List<PostDto> postDtoList = new ArrayList<>();
        switch (status) {
            case "inactive":
                postDtoList = inActivePosts(posts);
                break;
            case "pending":
                postDtoList = pendingPosts(posts);
                break;
            case "declined":
                postDtoList = declinedPosts(posts);
                break;
            case "published":
                postDtoList = publishedPosts(posts);
                break;
        }
        postDtoList = getCollectionsByOffsetLimit(offset, limit, postDtoList);
        postResponse.setCount(postDtoList.size());
        postResponse.setPostsDto(postDtoList);
        return postResponse;
    }

    public PostResponse getPostsModeration(String status, int offset, int limit) {
        PostResponse response = new PostResponse();
        List<Post> allPosts = new ArrayList<>();
        switch (status) {
            case "new":
                allPosts = postRepository.findNewPosts();
                break;
            case "declined":
                allPosts = postRepository.findDeclinedPosts();
                break;
            case "accepted":
                allPosts = postRepository.findAcceptedPosts();
                break;
        }
        List<PostDto> postDtoList = preparePost(allPosts);
        postDtoList = getCollectionsByOffsetLimit(offset, limit, postDtoList);
        response.setPostsDto(postDtoList);
        response.setCount(postDtoList.size());
        return response;
    }

    public RegisterResponse addPost(long timestamp, short active, String title, HashSet<String> tags, String text) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        RegisterResponse response = new RegisterResponse();
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail).get();
        Map<String, String> errors = checkTitleAndText(text, title);
        if (!errors.isEmpty()) {
            response.setErrors(errors);
            response.setResult(false);
            return response;
        }
        Post post = new Post();
        if (System.currentTimeMillis() / 1000L > timestamp) {
            post.setTime(LocalDateTime.now());
        } else {
            LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC);
            post.setTime(localDateTime);
        }

        post.setActive(active);
        post.setModerationStatus(ModerationStatus.NEW);
        post.setText(text);
        post.setTitle(title);
        post.setViewCount(0);
        post.setUser(user);
        postRepository.save(post);
        setTagsToPost(tags, post);
        response.setResult(true);
        return response;
    }

    private void setTagsToPost(Set<String> tags, Post post) {
        for (String str : tags) {
            Optional<Tag> tagOptional = tagRepository.findByName(str);
            if (tagOptional.isPresent()) {
                Tag2Post tag2Post = new Tag2Post();
                tag2Post.setPost(post);
                tag2Post.setTag(tagOptional.get());
                tag2PostRepository.save(tag2Post);
            }
        }
    }

    private Map<String, String> checkTitleAndText(String text, String title) {
        Map<String, String> result = new HashMap<>();
        if (text.isEmpty()) {
            result.put("text", "Текст не установлен");
        }
        if (text.length() < 50) {
            result.put("text", "Текст публикации слишком короткий");
        }
        if (title.isEmpty()) {
            result.put("title", "Заголовок не установлен");
        }
        if (title.length() < 3) {
            result.put("title", "Заголовок публикации слишком короткий");
        }
        return result;
    }

    private List<PostDto> inActivePosts(List<Post> posts) {
        List<Post> postList = new ArrayList<>();
        for (Post post : posts) {
            if (post.getActive() == 0) {
                postList.add(post);
            }
        }
        return preparePost(postList);
    }

    private List<PostDto> pendingPosts(List<Post> posts) {
        List<Post> postList = new ArrayList<>();
        for (Post post : posts) {
            if (post.getActive() == 1 && post.getModerationStatus().toString().equals("NEW")) {
                postList.add(post);
            }
        }
        return preparePost(postList);
    }

    private List<PostDto> declinedPosts(List<Post> posts) {
        List<Post> postList = new ArrayList<>();
        for (Post post : posts) {
            if (post.getActive() == 1 && post.getModerationStatus().toString().equals("DECLINED")) {
                postList.add(post);
            }
        }
        return preparePost(postList);
    }

    private List<PostDto> publishedPosts(List<Post> posts) {
        List<Post> postList = new ArrayList<>();
        for (Post post : posts) {
            if (post.getActive() == 1 && post.getModerationStatus().toString().equals("ACCEPTED")) {
                postList.add(post);
            }
        }
        return preparePost(postList);
    }

    private PostDtoById preparePost(Post post) {
        PostDtoById postDto = dtoMapper.postDtoById(post);
        UserPostsDto user = postDto.getUser();
        user.setPhoto(null);
        setTags(post, postDto);
        setComments(post, postDto);
        Duration duration = Duration.between(post.getTime(), LocalDateTime.now());
        long secondsAfterCreatePost = (System.currentTimeMillis() / 1000L) - duration.getSeconds();
        postDto.setTimestamp(secondsAfterCreatePost);
        setPostByIdDtoVotesCount(post, postDto);
        incrementViewCount(post, postDto);
        return postDto;
    }

    private void incrementViewCount(Post post, PostDtoById postDto) {
        User userPost = post.getUser();
        String emailAuthUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userPost.getModerator() == 0 && !emailAuthUser.equals(userPost.getEmail())) {
            int viewCount = post.getViewCount() + 1;
            postDto.setViewCount(viewCount);
            post.setViewCount(viewCount);
            postRepository.save(post);
        }
    }

    private void setTags(Post post, PostDtoById postDto) {
        List<String> result = new ArrayList<>();
        List<Tag> tags = tagRepository.findAll();
        for (Tag tag : tags) {
            List<Post> posts = tag.getPosts();
            if (posts.contains(post)) {
                result.add(tag.getName());
            }
        }
        postDto.setTags(result);
    }

    private void setComments(Post post, PostDtoById postDto) {
        List<PostComments> comments = post.getPostComments();
        List<PostCommentDto> result = new ArrayList<>();

        for (PostComments comment : comments) {
            UserPostsDto userPostsDto = dtoMapper.userToUserDtoForPosts(post.getUser());
            PostCommentDto commentDto = dtoMapper.postCommentToDto(comment);

            Duration duration = Duration.between(comment.getTime(), LocalDateTime.now());
            long secondsAfterCreatePost = (System.currentTimeMillis() / 1000L) - duration.getSeconds();

            commentDto.setTimestamp(secondsAfterCreatePost);
            commentDto.setUser(userPostsDto);
            result.add(commentDto);
        }

        postDto.setComments(result);
    }

    private List<Post> findPostByTag(String tagName) {
        List<Tag> tags = tagRepository.findAll();
        List<Post> result = new ArrayList<>();
        for (Tag tag : tags) {
            if (tag.getName().equals(tagName)) {
                result.addAll(tag.getPosts());
            }
        }
        return result;
    }

    private List<Post> findPostByDate(List<Post> allPosts, String date) {
        DateTimeFormatter formatter = CommonConstants.FORMATTER;
        List<Post> result = new ArrayList<>();
        for (Post post : allPosts) {
            String datePost = post.getTime().format(formatter);
            if (datePost.equals(date)) {
                result.add(post);
            }
        }
        return result;
    }

    private List<Post> findPostWithQuery(String query) {
        List<Post> allPosts = postRepository.findAll();
        List<Post> result = new ArrayList<>();
        for (Post post : allPosts) {
            boolean isContain = (Pattern.compile(Pattern.quote(query), Pattern.CASE_INSENSITIVE)
                    .matcher(post.getText()).find()) ||
                    (Pattern.compile(Pattern.quote(query), Pattern.CASE_INSENSITIVE)
                            .matcher(post.getTitle()).find());
            if (isContain) {
                result.add(post);
            }
        }
        return result;
    }

    private List<PostDto> preparePost(List<Post> posts) {
        List<PostDto> result = new ArrayList<>();
        for (Post post : posts) {
            PostDto postDto = dtoMapper.postToPostDto(post);
            UserPostsDto user = postDto.getUser();
            user.setPhoto(null);
            postDto.setUser(user);
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

    private void setPostByIdDtoVotesCount(Post post, PostDtoById postDto) {
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
        long secondsAfterCreatePost = (System.currentTimeMillis() / 1000L) - duration.getSeconds();
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
        return (int) (o2.getTimestamp() - o1.getTimestamp());
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
        return (int) (o1.getTimestamp() - o2.getTimestamp());
    }
}

