package org.example.userservice.map;

import org.example.userservice.dto.request.AdminRequestDto;
import org.example.userservice.dto.response.AdminDto;
import org.example.userservice.model.Admin;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface AdminMap {

    AdminDto toAdminDto(Admin admin);
    Admin toEntity(AdminDto adminDto);
    Admin fromRequest(AdminRequestDto adminRequestDto);
    List<AdminDto> toDto(List<Admin> admins);
}
