package com.javlasov.blog.mappers;

import com.javlasov.blog.dto.PostDto;
import com.javlasov.blog.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {

    PostDto toDto(Post post);

    List<PostDto> toDtoList(List<Post> posts);
}
