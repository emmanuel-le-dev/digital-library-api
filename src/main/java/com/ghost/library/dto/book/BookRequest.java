package com.ghost.library.dto.book;

import java.time.LocalDate;
import java.util.Set;

public record BookRequest(
    
    String isbn,
    
    String title,
    
    String description,
    
    LocalDate publicationDate,
    
    Integer totalCopies,
    
    String language,
    
    Integer pageCount,
    
    Long categoryId,
    
    Set<Long> authorIds
    
) {}
