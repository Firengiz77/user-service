package org.example.userservice.map;

import org.example.userservice.dto.UserDto;
import org.example.userservice.model.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Optional;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface  UserMap {

    UserDto toUserDto(User user);
    User toEntity(UserDto user);

    List<UserDto> toDto(List<User> users);
    List<User> toEntity(List<UserDto> userDto);
}
