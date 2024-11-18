package org.example.userservice.map;

import org.example.userservice.dto.request.UserRequestDto;
import org.example.userservice.dto.response.UserDto;
import org.example.userservice.model.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface  UserMap {
    UserDto toUserDto(User user);
    User toEntity(UserDto user);
    User fromRequest(UserRequestDto userRequestDto);
    List<UserDto> toDto(List<User> users);

}
