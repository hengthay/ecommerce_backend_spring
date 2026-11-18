package com.hengthay.ecommercebackendspring.categories;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "active", source = "active")
    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequest request);

    void update(UpdateCategoryRequest request, @MappingTarget Category category);
}
