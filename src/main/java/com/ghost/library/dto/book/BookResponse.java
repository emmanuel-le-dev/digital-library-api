package com.ghost.library.dto.book;

import java.time.LocalDate;
import java.util.Set;

import com.ghost.library.dto.author.AuthorSummaryResponse;

public record BookResponse(
    
    Long id,
    
    String isbn,
    
    String title,
    
    String description,
    
    LocalDate publicationDate,
    
    Integer totalCopies,
    
    Integer availableCopies,
    
    String language,
    
    Integer pageCount,
    
    Long categoryId,
    
    String categoryName,
    
    Set<AuthorSummaryResponse> authors
    
) {}
