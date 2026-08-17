package com.ghost.library.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.ghost.library.dto.category.CategoryRequest;
import com.ghost.library.dto.category.CategoryResponse;
import com.ghost.library.entity.Category;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface CategoryMapper {

    CategoryResponse toResponse(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    void updateEntity(
        CategoryRequest request,
        @MappingTarget Category category
    );
    
}
