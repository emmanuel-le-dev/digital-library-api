package com.ghost.library.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.ghost.library.dto.loan.LoanRequest;
import com.ghost.library.dto.loan.LoanResponse;
import com.ghost.library.entity.Loan;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface LoanMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    @Mapping(
        target = "userFullName",
        expression = "java(loan.getUser().getFirstName() + \" \" + loan.getUser().getLastName())"
    )
    LoanResponse toResponse(Loan loan);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "book", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateEntity(
        LoanRequest request,
        @MappingTarget Loan loan
    );
    
}
