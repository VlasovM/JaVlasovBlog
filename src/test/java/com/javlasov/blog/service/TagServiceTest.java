package com.javlasov.blog.service;

import com.javlasov.blog.api.response.TagResponse;
import com.javlasov.blog.dto.TagDto;
import com.javlasov.blog.mappers.DtoMapper;
import com.javlasov.blog.model.Post;
import com.javlasov.blog.model.Tag;
import com.javlasov.blog.model.Tag2Post;
import com.javlasov.blog.repository.PostRepository;
import com.javlasov.blog.repository.Tag2PostRepository;
import com.javlasov.blog.repository.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class TagServiceTest {

    PostRepository mockPostRepo = Mockito.mock(PostRepository.class);
    TagRepository mockTagRepo = Mockito.mock(TagRepository.class);
    Tag2PostRepository mockTag2PostRepo = Mockito.mock(Tag2PostRepository.class);
    DtoMapper mockDtoMapper = Mockito.mock(DtoMapper.class);

    TagService underTestService = new TagService(mockDtoMapper, mockTagRepo,
            mockPostRepo, mockTag2PostRepo);

    @Test
    @DisplayName("Get all tags. Query's empty.")
    void getAllTagsWithoutQueryTest() {
        TagResponse expectedResponse = new TagResponse();
        expectedResponse.setTags(getExpectedTagsList());

        List<Tag> allTagsList = getTagsList();
        when(mockTagRepo.findAll()).thenReturn(allTagsList);

        List<Post> allPostsList = getPostsList();
        when(mockPostRepo.findAll()).thenReturn(allPostsList);

        List<Tag2Post> tagJavaToPostsList = getTag2PostJava();
        when(mockTag2PostRepo.findByTagId(allTagsList.get(0).getId())).thenReturn(tagJavaToPostsList);

        List<Tag2Post> tagSQLToPostsList = getTag2PostSQL();
        when(mockTag2PostRepo.findByTagId(allTagsList.get(1).getId())).thenReturn(tagSQLToPostsList);

        List<TagDto> tagsDtoList = getTagsDtoList();
        when(mockDtoMapper.tagToTagDto(allTagsList.get(0))).thenReturn(tagsDtoList.get(0));
        when(mockDtoMapper.tagToTagDto(allTagsList.get(1))).thenReturn(tagsDtoList.get(1));
        when(mockDtoMapper.tagToTagDto(allTagsList.get(2))).thenReturn(tagsDtoList.get(2));
        when(mockDtoMapper.tagToTagDto(allTagsList.get(3))).thenReturn(tagsDtoList.get(3));

        TagResponse actualResponse = underTestService.tag(Optional.empty());

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Get all tags with query")
    void getTagsWithQuery() {
        TagResponse expectedResponse = new TagResponse();
        expectedResponse.setTags(getExpectedTagsListWithQuery());

        List<Tag> allTagsList = getTagsList();
        when(mockTagRepo.findAll()).thenReturn(allTagsList);

        List<Post> allPostsList = getPostsList();
        when(mockPostRepo.findAll()).thenReturn(allPostsList);

        List<TagDto> tagDtoList = getTagsDtoList();
        when(mockDtoMapper.tagToTagDto(allTagsList.get(0))).thenReturn(tagDtoList.get(0));
        when(mockDtoMapper.tagToTagDto(allTagsList.get(3))).thenReturn(tagDtoList.get(3));

        List<Tag2Post> tagJavaToPostsList = getTag2PostJava();
        when(mockTag2PostRepo.findByTagId(allTagsList.get(0).getId())).thenReturn(tagJavaToPostsList);

        String query = "Java";

        TagResponse actualResponse = underTestService.tag(Optional.of(query));

        assertEquals(expectedResponse, actualResponse);

    }

    private List<Tag> getTagsList() {
        List<Tag> tagsList = new ArrayList<>();

        Tag tagOne = new Tag();
        Tag tagTwo = new Tag();
        Tag tagThree = new Tag();
        Tag tagFour = new Tag();

        tagOne.setId(0);
        tagTwo.setId(1);
        tagThree.setId(2);
        tagFour.setId(3);

        tagOne.setName("Java");
        tagTwo.setName("SQL");
        tagThree.setName("Hibernate");
        tagFour.setName("JavaScript");

        tagsList.add(tagOne);
        tagsList.add(tagTwo);
        tagsList.add(tagThree);
        tagsList.add(tagFour);

        return tagsList;
    }

    private List<Post> getPostsList() {
        List<Post> postsList = new ArrayList<>();

        Post postOne = new Post();
        Post postTwo = new Post();
        Post postThree = new Post();

        postOne.setId(0);
        postTwo.setId(1);
        postThree.setId(2);

        postsList.add(postOne);
        postsList.add(postTwo);
        postsList.add(postThree);

        return postsList;

    }

    private List<TagDto> getTagsDtoList() {
        List<TagDto> tagsDtoList = new ArrayList<>();

        TagDto tagDtoJava = new TagDto();
        TagDto tagDtoSQL = new TagDto();
        TagDto tagDtoHibernate = new TagDto();
        TagDto tagDtoJavaScript = new TagDto();

        tagDtoJava.setName("Java");
        tagDtoSQL.setName("SQL");
        tagDtoHibernate.setName("Hibernate");
        tagDtoJavaScript.setName("JavaScript");

        tagsDtoList.add(tagDtoJava);
        tagsDtoList.add(tagDtoSQL);
        tagsDtoList.add(tagDtoHibernate);
        tagsDtoList.add(tagDtoJavaScript);

        return tagsDtoList;
    }

    private List<Tag2Post> getTag2PostJava() {
        List<Post> postsList = getPostsList();
        List<Tag> tagsList = getTagsList();

        List<Tag2Post> tagJava2PostList = new ArrayList<>();
        Tag2Post tag2PostOne = new Tag2Post();
        tag2PostOne.setPost(postsList.get(0));
        tag2PostOne.setTag(tagsList.get(0));
        tag2PostOne.setId(0);

        Tag2Post tag2PostTwo = new Tag2Post();
        tag2PostTwo.setPost(postsList.get(1));
        tag2PostTwo.setTag(tagsList.get(0));
        tag2PostTwo.setId(1);

        tagJava2PostList.add(tag2PostOne);
        tagJava2PostList.add(tag2PostTwo);

        return tagJava2PostList;
    }

    private List<Tag2Post> getTag2PostSQL() {
        List<Post> postsList = getPostsList();
        List<Tag> tagsList = getTagsList();

        List<Tag2Post> tagSQL2Post = new ArrayList<>();
        Tag2Post tag2PostOne = new Tag2Post();
        tag2PostOne.setPost(postsList.get(0));
        tag2PostOne.setTag(tagsList.get(1));
        tag2PostOne.setId(0);


        tagSQL2Post.add(tag2PostOne);

        return tagSQL2Post;
    }

    private List<TagDto> getExpectedTagsList() {
        List<TagDto> tagsExpected = new ArrayList<>();

        TagDto tagDtoJava = new TagDto();
        TagDto tagDtoSQL = new TagDto();
        TagDto tagDtoHibernate = new TagDto();
        TagDto tagDtoJavaScript = new TagDto();

        tagDtoJava.setName("Java");
        tagDtoSQL.setName("SQL");
        tagDtoHibernate.setName("Hibernate");
        tagDtoJavaScript.setName("JavaScript");

        tagDtoJava.setWeight(1.0);
        tagDtoSQL.setWeight(0.5);
        tagDtoHibernate.setWeight(0.0);
        tagDtoJavaScript.setWeight(0.0);

        tagsExpected.add(tagDtoJava);
        tagsExpected.add(tagDtoSQL);
        tagsExpected.add(tagDtoHibernate);
        tagsExpected.add(tagDtoJavaScript);

        return tagsExpected;
    }

    private List<TagDto> getExpectedTagsListWithQuery() {
        List<TagDto> tagsExpected = new ArrayList<>();

        TagDto tagJava = new TagDto();
        tagJava.setName("Java");
        tagJava.setWeight(1.0);

        TagDto tagJavaScript = new TagDto();
        tagJavaScript.setName("JavaScript");
        tagJavaScript.setWeight(0.0);

        tagsExpected.add(tagJava);
        tagsExpected.add(tagJavaScript);

        return tagsExpected;
    }

}