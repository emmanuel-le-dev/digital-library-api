package com.ghost.library.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.ghost.library.dto.author.AuthorSummaryResponse;
import com.ghost.library.dto.book.BookRequest;
import com.ghost.library.dto.book.BookResponse;
import com.ghost.library.entity.Author;
import com.ghost.library.entity.Book;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface BookMapper {
    
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    BookResponse toResponse(Book book);

    AuthorSummaryResponse toSummary(Author author);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "loans", ignore = true)
    @Mapping(target = "authors", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "availableCopies", ignore = true)
    void updateEntity(
        BookRequest request,
        @MappingTarget Book book
    );

}
