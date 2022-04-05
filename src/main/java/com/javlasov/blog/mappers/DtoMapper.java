package com.javlasov.blog.mappers;

import com.javlasov.blog.dto.PostDto;
import com.javlasov.blog.dto.TagDto;
import com.javlasov.blog.entity.Post;
import com.javlasov.blog.entity.Tag;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DtoMapper {

    PostDto PostToPostDto(Post post);

    List<PostDto> PostListToPostDtoList(List<Post> posts);

    TagDto TagToTagDto(Tag tag);

    List<TagDto> TagDtoToTagDtoList(List<Tag> tags);
}
