package com.javlasov.blog.mappers;

import com.javlasov.blog.dto.*;
import com.javlasov.blog.model.Post;
import com.javlasov.blog.model.PostComments;
import com.javlasov.blog.model.Tag;
import com.javlasov.blog.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DtoMapper {

    PostDto postToPostDto(Post post);

    TagDto tagToTagDto(Tag tag);

    @Mapping(source = "active", target = "active", ignore = true)
    PostDtoById postDtoById(Post post);

    PostCommentDto postCommentToDto(PostComments comment);

    UserDtoForPosts userToUserDtoForPosts(User user);

    UserDto userToUserDTO(User user);

}
