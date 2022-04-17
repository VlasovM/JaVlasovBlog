package com.javlasov.blog.mappers;

import com.javlasov.blog.dto.*;
import com.javlasov.blog.entity.Post;
import com.javlasov.blog.entity.PostComments;
import com.javlasov.blog.entity.Tag;
import com.javlasov.blog.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DtoMapper {

    PostDto postToPostDto(Post post);

    TagDto tagToTagDto(Tag tag);

    @Mapping(source = "active", target = "active", ignore = true)
    PostDtoById postDtoById(Post post);

    PostCommentDto postCommentToDto(PostComments comment);

    UserDto userToUserDto(User user);

}
