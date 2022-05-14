package dev.arunangshu.api.mappers;

import dev.arunangshu.api.model.UserDto;
import dev.arunangshu.domain.User;
import org.mapstruct.Mapper;

@Mapper(uses = {DateMapper.class}, componentModel = "cdi")
public interface UserMapper {

    UserDto userToUserDto(User user);

    User userDtoToUser(UserDto userDto);
}
