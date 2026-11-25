package com.hengthay.ecommercebackendspring.addresses;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    @Mapping(target = "userDto", source = "user")
    AddressDto toDto(Address address);

    Address toEntity(CreateAddressRequest request);

    void update(UpdateAddressRequest request, @MappingTarget Address address);
}
