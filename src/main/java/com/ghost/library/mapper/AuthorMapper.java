package com.ghost.library.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.ghost.library.dto.author.AuthorRequest;
import com.ghost.library.dto.author.AuthorResponse;
import com.ghost.library.entity.Author;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface AuthorMapper {

    AuthorResponse toResponse(Author author);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    void updateEntity(
        AuthorRequest request,
        @MappingTarget Author author
    );
    
}
