package com.connect.user.application.mapper;

import com.connect.user.application.dto.PostgresUserDto;
import com.connect.user.domain.entities.PostgresUser;
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
    PostgresUserDto userToUserDto(PostgresUser user);
}
