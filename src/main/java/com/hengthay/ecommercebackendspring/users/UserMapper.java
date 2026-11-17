package com.hengthay.ecommercebackendspring.users;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    // Convert User Entity to Dto
    @Mapping(target = "active", source = "active")
    UserDto toDto(User user);
    // Convert from Dto to Entity for save in DB
    User toEntity(RegisterUserRequest request);
    // Update
    void update(UpdateUserRequest request, @MappingTarget User user);
    // Admin Update Role
    void updateRole(AdminUpdateUserRoleRequest request, @MappingTarget User user);
}
