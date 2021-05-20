package com.connect.dtos.mappers;

import com.connect.dtos.UserDto;
import com.connect.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "nickname", target = "nickname")
    @Mapping(source = "profilePicture", target = "profilePicture")
    UserDto userToUserDto(User user);
}
