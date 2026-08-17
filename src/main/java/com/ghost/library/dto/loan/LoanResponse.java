package com.ghost.library.dto.loan;

import java.time.LocalDateTime;

import com.ghost.library.entity.LoanStatus;

public record LoanResponse(
    
    Long id,
    
    LocalDateTime borrowedAt,
    
    LocalDateTime dueAt,
    
    LocalDateTime returnedAt,
    
    LoanStatus status,
    
    Long userId,
    
    String userFullName,
    
    Long bookId,
    
    String bookTitle
    
) {}
