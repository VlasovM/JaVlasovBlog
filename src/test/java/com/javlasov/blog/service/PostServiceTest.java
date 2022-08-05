package com.javlasov.blog.service;

import com.javlasov.blog.api.response.PostResponse;
import com.javlasov.blog.api.response.StatusResponse;
import com.javlasov.blog.dto.*;
import com.javlasov.blog.mappers.DtoMapper;
import com.javlasov.blog.model.*;
import com.javlasov.blog.model.enums.ModerationStatus;
import com.javlasov.blog.repository.PostRepository;
import com.javlasov.blog.repository.Tag2PostRepository;
import com.javlasov.blog.repository.TagRepository;
import com.javlasov.blog.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

class PostServiceTest {

    PostRepository mockPostRepo = Mockito.mock(PostRepository.class);
    UserRepository mockUserRepo = Mockito.mock(UserRepository.class);
    TagRepository mockTagRepo = Mockito.mock(TagRepository.class);
    Tag2PostRepository mockTag2PostRepo = Mockito.mock(Tag2PostRepository.class);
    DtoMapper mockDtoMapper = Mockito.mock(DtoMapper.class);
    PostService underTestService = new PostService(
            mockPostRepo, mockDtoMapper, mockTagRepo, mockUserRepo, mockTag2PostRepo);

    @Test
    @DisplayName("Get all posts.")
    void getAllPostsTest() {
        PostResponse expectedResponse = new PostResponse();
        expectedResponse.setCount(getExpectedPostDtoList().size());
        expectedResponse.setPostsDto(getExpectedPostDtoList());

        List<Post> postList = getPostsList();
        when(mockPostRepo.findAll()).thenReturn(postList);

        List<PostDto> postDtoList = getPostDtoList();
        when(mockDtoMapper.postToPostDto(postList.get(0))).thenReturn(postDtoList.get(0));

        PostResponse actualResponse = underTestService.getAllPosts("recent", 0, 5);

        assertEquals(expectedResponse, actualResponse);

    }

    @Test
    @DisplayName("Not found matches with query.")
    void getPostWithQueryNotFoundMatchesTest() {
        PostResponse expectedResponse = new PostResponse();
        expectedResponse.setPostsDto(new ArrayList<>());

        String notFoundMatches = "NotFound";

        List<Post> allPosts = getPostsList();
        when(mockPostRepo.findAll()).thenReturn(allPosts);

        PostResponse actualResponse = underTestService.postSearch(notFoundMatches, 0, 5);

        assertEquals(expectedResponse, actualResponse);

    }

    @Test
    @DisplayName("Find post with query.")
    void getPostWithQueryTest() {
        PostResponse expectedResponse = new PostResponse();
        expectedResponse.setCount(getExpectedPostDtoList().size());
        expectedResponse.setPostsDto(getExpectedPostDtoList());

        String query = "Integer";

        List<Post> postList = getPostsList();
        when(mockPostRepo.findAll()).thenReturn(postList);

        List<PostDto> postDtoList = getPostDtoList();
        when(mockDtoMapper.postToPostDto(postList.get(0))).thenReturn(postDtoList.get(0));

        PostResponse actualResponse = underTestService.postSearch(query, 0, 5);

        assertEquals(expectedResponse, actualResponse);

    }

    @Test
    @DisplayName("Get post by correct date.")
    void getPostByCorrectDateTest() {
        PostResponse expectedResponse = new PostResponse();
        expectedResponse.setPostsDto(getExpectedPostDtoList());
        expectedResponse.setCount(getExpectedPostDtoList().size());

        List<Post> postList = getPostsList();
        when(mockPostRepo.findAll()).thenReturn(postList);

        List<PostDto> postDtoList = getPostDtoList();
        when(mockDtoMapper.postToPostDto(postList.get(0))).thenReturn(postDtoList.get(0));

        PostResponse actualResponse = underTestService.getPostByDate("2022-08-01", 0, 5);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Get post by incorrect date.")
    void getPostByIncorrectDateTest() {
        PostResponse expectedResponse = new PostResponse();
        expectedResponse.setPostsDto(new ArrayList<>());

        List<Post> postList = getPostsList();
        when(mockPostRepo.findAll()).thenReturn(postList);

        PostResponse actualResponse = underTestService.getPostByDate("1999-12-31", 0, 5);

        assertEquals(expectedResponse, actualResponse);

    }

    @Test
    @DisplayName("Get post by correct tag.")
    void getPostByCorrectTagTest() {
        PostResponse expectedResponse = new PostResponse();
        expectedResponse.setPostsDto(getExpectedPostDtoList());
        expectedResponse.setCount(getExpectedPostDtoList().size());

        Tag tagJava = getTag();
        when(mockTagRepo.findByName(tagJava.getName())).thenReturn(Optional.of(tagJava));

        List<Tag2Post> tag2PostList = getTag2PostList();
        when(mockTag2PostRepo.findByTagId(tagJava.getId())).thenReturn(tag2PostList);

        List<Post> postList = getPostsList();
        when(mockPostRepo.findById(postList.get(0).getId())).thenReturn(Optional.of(postList.get(0)));

        List<PostDto> postDtoList = getPostDtoList();
        when(mockDtoMapper.postToPostDto(postList.get(0))).thenReturn(postDtoList.get(0));

        PostResponse actualResponse = underTestService.getPostByTag("Java", 0, 5);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Get post by incorrect tag.")
    void getPostByIncorrectTagTest() {
        PostResponse expectedResponse = new PostResponse();
        expectedResponse.setPostsDto(new ArrayList<>());

        Tag tagJava = getTag();
        when(mockTagRepo.findByName(tagJava.getName())).thenReturn(Optional.of(tagJava));

        PostResponse actualResponse = underTestService.getPostByTag(tagJava.getName(), 0, 5);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Get post by id.")
    void getPostByIdTest() {
        PostDtoById expectedResponse = getExpectedPostDtoById();

        User user = getUsersList().get(0);
        when(mockUserRepo.findById(user.getId())).thenReturn(Optional.of(user));

        List<PostComments> postCommentsList = getPostCommentsList();
        PostComments postComments = postCommentsList.get(0);
        postComments.setUserId(user.getId());
        postComments.setTime(LocalDateTime.now().minusHours(1));

        Post post = getPostsList().get(0);
        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(getTag());
        post.setTags(tagSet);
        post.setPostComments(postCommentsList);

        PostCommentDto postCommentDto = new PostCommentDto();
        when(mockDtoMapper.postCommentToDto(postComments)).thenReturn(postCommentDto);

        UserPostDto userPostDto = getUsersPostDtoList().get(0);
        when(mockDtoMapper.userToUserDtoForPosts(user)).thenReturn(userPostDto);

        when(mockPostRepo.getById(post.getId())).thenReturn(post);

        PostDtoById postDtoById = getPostDtoById();
        when(mockDtoMapper.postDtoById(post)).thenReturn(postDtoById);

        UserDto userDto = new UserDto();
        userDto.setId(0);
        when(mockDtoMapper.userToUserDTO(user)).thenReturn(userDto);

        PostDtoById actualResponse = underTestService.getPostById(post.getId());

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Get inactive posts auth user.")
    void getMyInActivePostsTest() {
        PostResponse expectedResponse = new PostResponse();
        expectedResponse.setPostsDto(getExpectedPostDtoList());
        expectedResponse.setCount(getExpectedPostDtoList().size());

        User user = getUsersList().get(0);
        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        List<Post> posts = getPostsList();
        posts.get(0).setActive((short) 0);
        when(mockPostRepo.findPostsByUserId(user.getId())).thenReturn(posts);

        List<PostDto> postDtoList = getPostDtoList();
        when(mockDtoMapper.postToPostDto(posts.get(0))).thenReturn(postDtoList.get(0));


        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getEmail());

        PostResponse actualResponse = underTestService.getMyPosts("inactive", 0, 5);

        assertEquals(expectedResponse, actualResponse);

    }

    @Test
    @DisplayName("Get pending posts auth user.")
    void getMyPendingPostsTest() {
        PostResponse expectedResponse = new PostResponse();
        expectedResponse.setPostsDto(getExpectedPostDtoList());
        expectedResponse.setCount(getExpectedPostDtoList().size());

        User user = getUsersList().get(0);
        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        List<Post> posts = getPostsList();
        posts.get(0).setActive((short) 1);
        posts.get(0).setModerationStatus(ModerationStatus.NEW);
        when(mockPostRepo.findPostsByUserId(user.getId())).thenReturn(posts);

        List<PostDto> postDtoList = getPostDtoList();
        when(mockDtoMapper.postToPostDto(posts.get(0))).thenReturn(postDtoList.get(0));


        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getEmail());

        PostResponse actualResponse = underTestService.getMyPosts("pending", 0, 5);

        assertEquals(expectedResponse, actualResponse);

    }

    @Test
    @DisplayName("Get declined posts auth user.")
    void getMyDeclinedPostsTest() {
        PostResponse expectedResponse = new PostResponse();
        expectedResponse.setPostsDto(getExpectedPostDtoList());
        expectedResponse.setCount(getExpectedPostDtoList().size());

        User user = getUsersList().get(0);
        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        List<Post> posts = getPostsList();
        posts.get(0).setActive((short) 1);
        posts.get(0).setModerationStatus(ModerationStatus.DECLINED);
        when(mockPostRepo.findPostsByUserId(user.getId())).thenReturn(posts);

        List<PostDto> postDtoList = getPostDtoList();
        when(mockDtoMapper.postToPostDto(posts.get(0))).thenReturn(postDtoList.get(0));


        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getEmail());

        PostResponse actualResponse = underTestService.getMyPosts("declined", 0, 5);

        assertEquals(expectedResponse, actualResponse);

    }

    @Test
    @DisplayName("Get published posts auth user.")
    void getMyPublishedPostsTest() {
        PostResponse expectedResponse = new PostResponse();
        expectedResponse.setPostsDto(getExpectedPostDtoList());
        expectedResponse.setCount(getExpectedPostDtoList().size());

        User user = getUsersList().get(0);
        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        List<Post> posts = getPostsList();
        posts.get(0).setActive((short) 1);
        posts.get(0).setModerationStatus(ModerationStatus.ACCEPTED);
        when(mockPostRepo.findPostsByUserId(user.getId())).thenReturn(posts);

        List<PostDto> postDtoList = getPostDtoList();
        when(mockDtoMapper.postToPostDto(posts.get(0))).thenReturn(postDtoList.get(0));


        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getEmail());

        PostResponse actualResponse = underTestService.getMyPosts("published", 0, 5);

        assertEquals(expectedResponse, actualResponse);

    }

    @Test
    @DisplayName("Get empty list my posts.")
    void getMyPostsEmptyListTest() {
        PostResponse expectedResponse = new PostResponse();
        expectedResponse.setPostsDto(new ArrayList<>());

        User user = getUsersList().get(0);
        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        List<Post> posts = getPostsList();
        posts.get(0).setActive((short) 0);
        when(mockPostRepo.findPostsByUserId(user.getId())).thenReturn(posts);

        List<PostDto> postDtoList = getPostDtoList();
        when(mockDtoMapper.postToPostDto(posts.get(0))).thenReturn(postDtoList.get(0));


        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getEmail());

        PostResponse actualResponse = underTestService.getMyPosts("declined", 0, 5);

        assertEquals(expectedResponse, actualResponse);

    }

    @Test
    @DisplayName("Get posts for moderator.")
    void getPostsModerationTest() {
        PostResponse expectedResponse = new PostResponse();
        expectedResponse.setPostsDto(getExpectedPostDtoList());
        expectedResponse.setCount(getExpectedPostDtoList().size());

        List<Post> postList = getPostsList();
        when(mockPostRepo.findNewPosts()).thenReturn(postList);

        List<PostDto> postDtoList = getPostDtoList();
        when(mockDtoMapper.postToPostDto(postList.get(0))).thenReturn(postDtoList.get(0));

        PostResponse actualResponse = underTestService.getPostsModeration("new", 0, 5);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Add post.")
    void addPostTest() {
        StatusResponse expectedResponse = new StatusResponse();
        expectedResponse.setResult(true);

        User user = getUsersList().get(0);
        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));


        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getEmail());

        Post post = getAddedPost();
        List<String> tags = new ArrayList<>();
        tags.add("Java");

        StatusResponse actualResponse = underTestService.addPost(System.currentTimeMillis(), post.getActive(),
                post.getTitle(), tags, post.getText());

        assertEquals(expectedResponse, actualResponse);

    }

    @Test
    @DisplayName("Add post with incorrect title and text.")
    void addPostWithIncorrectTitleAndTextTest() {
        StatusResponse expectedResponse = new StatusResponse();
        Map<String, String> expectedErrorMap = new HashMap<>();
        expectedErrorMap.put("text", "Текст публикациии должен быть не менее 30 символов");
        expectedErrorMap.put("title", "Заголовок публикации должен быть не менее 3 символов и не более 50");
        expectedResponse.setErrors(expectedErrorMap);

        User user = getUsersList().get(0);
        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));


        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getEmail());

        Post post = getAddedPost();
        List<String> tags = new ArrayList<>();
        tags.add("Java");

        StatusResponse actualResponse = underTestService.addPost(System.currentTimeMillis(), post.getActive(),
                "T", tags, "T");

        assertEquals(expectedResponse, actualResponse);

    }

    @Test
    @DisplayName("Edit post. Check title before and after.")
    void editPostTest() {
        StatusResponse expectedResponse = new StatusResponse();
        expectedResponse.setResult(true);

        User user = getUsersList().get(0);
        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Post post = getAddedPost();
        when(mockPostRepo.getById(post.getId())).thenReturn(post);

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getEmail());

        List<String> tags = new ArrayList<>();
        tags.add("Java");

        String oldTitle = post.getTitle();

        StatusResponse actualResponse = underTestService.editPost(post.getId(), System.currentTimeMillis(),
                post.getActive(), "Change title. Check it.", tags, post.getText());

        assertNotEquals(oldTitle, post.getTitle());
        assertEquals(expectedResponse, actualResponse);

    }

    @Test
    @DisplayName("Edit post. Incorrect title and text.")
    void editPostWithIncorrectTitleAndTextTest() {
        StatusResponse expectedResponse = new StatusResponse();
        Map<String, String> expectedErrorMap = new HashMap<>();
        expectedErrorMap.put("text", "Текст публикациии должен быть не менее 30 символов");
        expectedErrorMap.put("title", "Заголовок публикации должен быть не менее 3 символов и не более 50");
        expectedResponse.setErrors(expectedErrorMap);

        User user = getUsersList().get(0);
        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Post post = getAddedPost();
        when(mockPostRepo.getById(post.getId())).thenReturn(post);

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getEmail());

        List<String> tags = new ArrayList<>();
        tags.add("Java");

        StatusResponse actualResponse = underTestService.editPost(post.getId(), System.currentTimeMillis(),
                post.getActive(), "T", tags, "T");

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Moderation post in accept decision. ")
    void moderationPostAcceptDecisionTest() {
        StatusResponse expectedResponse = new StatusResponse();

        User user = getUsersList().get(0);
        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Post post = getAddedPost();
        when(mockPostRepo.getById(post.getId())).thenReturn(post);

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getEmail());

        ModerationStatus oldStatus = post.getModerationStatus();

        StatusResponse actualResponse = underTestService.moderationPost(post.getId(), "accept");

        assertNotEquals(oldStatus, post.getModerationStatus());
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Moderation post in decline decision. ")
    void moderationPostDeclineDecisionTest() {
        StatusResponse expectedResponse = new StatusResponse();

        User user = getUsersList().get(0);
        when(mockUserRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Post post = getAddedPost();
        when(mockPostRepo.getById(post.getId())).thenReturn(post);

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getEmail());

        ModerationStatus oldStatus = post.getModerationStatus();

        StatusResponse actualResponse = underTestService.moderationPost(post.getId(), "decline");

        assertNotEquals(oldStatus, post.getModerationStatus());
        assertEquals(expectedResponse, actualResponse);
    }

    private List<User> getUsersList() {
        List<User> userList = new ArrayList<>();

        User userOne = new User();
        User userTwo = new User();
        User userThree = new User();

        userOne.setId(0);
        userTwo.setId(1);
        userThree.setId(2);

        userList.add(userOne);
        userList.add(userTwo);
        userList.add(userThree);

        return userList;
    }

    private List<UserPostDto> getUsersPostDtoList() {
        List<UserPostDto> userDtoList = new ArrayList<>();

        UserPostDto userDtoOne = new UserPostDto();
        UserPostDto userDtoTwo = new UserPostDto();
        UserPostDto userDtoThree = new UserPostDto();

        userDtoOne.setId(0);
        userDtoTwo.setId(1);
        userDtoThree.setId(2);

        userDtoList.add(userDtoOne);
        userDtoList.add(userDtoTwo);
        userDtoList.add(userDtoThree);

        return userDtoList;
    }

    private List<Post> getPostsList() {
        List<Post> postList = new ArrayList<>();
        List<PostVotes> postVotes = getPostVotesList();

        Post post = new Post();

        post.setId(0);

        post.setPostVotes(postVotes);
        post.setPostComments(getPostCommentsList());

        post.setTitle("Some title");
        post.setText("Cras auctor egestas fringilla." +
                "Integer maximus tellus sed dui mollis aliquet a quis mauris.");

        post.setTime(LocalDateTime.of(2022, 8, 1, 12, 0));

        postList.add(post);

        return postList;
    }

    private List<PostDto> getPostDtoList() {
        List<PostDto> postDtoList = new ArrayList<>();
        List<UserPostDto> userDtoList = getUsersPostDtoList();

        PostDto postDto = new PostDto();

        postDto.setId(0);
        postDto.setUser(userDtoList.get(0));
        postDto.setTitle("Some title");

        postDtoList.add(postDto);

        return postDtoList;
    }

    private List<PostVotes> getPostVotesList() {
        List<PostVotes> postVotesList = new ArrayList<>();

        PostVotes postVotesOne = new PostVotes();
        PostVotes postVotesTwo = new PostVotes();
        PostVotes postVotesThree = new PostVotes();

        postVotesOne.setPostId(0);
        postVotesOne.setUserId(0);
        postVotesOne.setValue(1);

        postVotesTwo.setPostId(0);
        postVotesTwo.setUserId(1);
        postVotesTwo.setValue(1);

        postVotesThree.setPostId(0);
        postVotesThree.setUserId(2);
        postVotesThree.setValue(-1);

        postVotesList.add(postVotesOne);
        postVotesList.add(postVotesTwo);
        postVotesList.add(postVotesThree);

        return postVotesList;
    }

    private List<PostComments> getPostCommentsList() {
        List<PostComments> postCommentsList = new ArrayList<>();

        PostComments postCommentsOne = new PostComments();

        postCommentsList.add(postCommentsOne);

        return postCommentsList;
    }

    private List<PostDto> getExpectedPostDtoList() {
        List<PostDto> postDtoListExpected = new ArrayList<>();

        PostDto postDtoExpected = new PostDto();
        postDtoExpected.setViewCount(0);
        postDtoExpected.setCommentCount(1);
        postDtoExpected.setAnnounce("Cras auctor egestas fringilla." +
                "Integer maximus tellus sed dui mollis aliquet a quis mauris....");
        postDtoExpected.setLikeCount(2);
        postDtoExpected.setDislikeCount(1);
        postDtoExpected.setTitle("Some title");

        LocalDateTime localDateTime = LocalDateTime.of(2022, 8, 1, 12, 0);
        Duration duration = Duration.between(localDateTime, LocalDateTime.now());
        long secondsAfterCreatePost = (System.currentTimeMillis() / 1000L) - duration.getSeconds();
        postDtoExpected.setTimestamp(secondsAfterCreatePost);

        UserPostDto userExpected = new UserPostDto();
        userExpected.setId(0);

        postDtoExpected.setUser(userExpected);
        postDtoListExpected.add(postDtoExpected);

        return postDtoListExpected;
    }

    private Tag getTag() {
        Tag tag = new Tag();
        tag.setName("Java");
        tag.setId(0);

        return tag;
    }

    private List<Tag2Post> getTag2PostList() {
        List<Tag2Post> tag2PostList = new ArrayList<>();
        Tag2Post tag2Post = new Tag2Post();

        Post post = getPostsList().get(0);
        Tag tag = getTag();

        tag2Post.setId(0);
        tag2Post.setTag(tag);
        tag2Post.setPost(post);

        tag2PostList.add(tag2Post);

        return tag2PostList;
    }

    private PostDtoById getPostDtoById() {
        Post post = getPostsList().get(0);
        UserPostDto userPostDto = getUsersPostDtoList().get(0);
        PostDtoById postDtoById = new PostDtoById();

        postDtoById.setUser(userPostDto);
        postDtoById.setTitle(post.getTitle());
        postDtoById.setText(post.getText());

        return postDtoById;
    }

    private PostDtoById getExpectedPostDtoById() {
        PostDtoById expectedPostDto = new PostDtoById();

        expectedPostDto.setTimestamp(0);
        expectedPostDto.setId(0);
        expectedPostDto.setActive(false);
        expectedPostDto.setTitle(null);
        expectedPostDto.setTitle("Some title");
        expectedPostDto.setText("Cras auctor egestas fringilla." +
                "Integer maximus tellus sed dui mollis aliquet a quis mauris.");
        expectedPostDto.setLikeCount(2);
        expectedPostDto.setDislikeCount(1);
        expectedPostDto.setViewCount(1);

        Set<String> tags = new HashSet<>();
        tags.add(getTag().getName());

        expectedPostDto.setTags(tags);

        UserPostDto userDto = new UserPostDto();
        userDto.setId(0);

        expectedPostDto.setUser(userDto);

        List<PostCommentDto> postCommentDtoList = new ArrayList<>();
        PostCommentDto postCommentDto = new PostCommentDto();
        Duration durationComment = Duration.between(LocalDateTime.now().minusHours(1), LocalDateTime.now());
        long timestampForComments = (System.currentTimeMillis() / 1000L) - durationComment.getSeconds();
        postCommentDto.setTimestamp(timestampForComments);
        postCommentDto.setUser(userDto);
        postCommentDtoList.add(postCommentDto);

        expectedPostDto.setComments(postCommentDtoList);

        Duration durationPost = Duration.between(
                LocalDateTime.of(2022, 8, 1, 12, 0), LocalDateTime.now());
        long timestampForPost = (System.currentTimeMillis() / 1000L) - durationPost.getSeconds();
        expectedPostDto.setTimestamp(timestampForPost);

        return expectedPostDto;
    }

    private Post getAddedPost() {
        Post post = new Post();
        User user = getUsersList().get(0);

        post.setId(0);
        post.setUser(user);
        post.setActive((short) 1);
        post.setTitle("Some title");
        post.setText("Voyager 1 is a space probe launched by NASA on September 5, 1977, " +
                "as part of the Voyager program to study the " +
                "outer Solar System and interstellar space beyond the Sun's heliosphere.");

        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(getTag());

        post.setTags(tagSet);

        return post;
    }

}