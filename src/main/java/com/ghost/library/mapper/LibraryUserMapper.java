package com.ghost.library.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.ghost.library.dto.user.LibraryUserRequest;
import com.ghost.library.dto.user.LibraryUserResponse;
import com.ghost.library.entity.LibraryUser;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface LibraryUserMapper {

    LibraryUserResponse toResponse(LibraryUser user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "loans", ignore = true)
    @Mapping(target = "active", ignore = true)
    void updateEntity(
        LibraryUserRequest request,
        @MappingTarget LibraryUser user
    );
    
}
