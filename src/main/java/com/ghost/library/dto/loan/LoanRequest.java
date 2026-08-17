package com.ghost.library.dto.loan;

import java.time.LocalDateTime;

import com.ghost.library.entity.LoanStatus;

public record LoanRequest(
    
    Long userId,
    
    Long bookId,
    
    LocalDateTime borrowedAt,
    
    LocalDateTime dueAt,
    
    LocalDateTime returnedAt,
    
    LoanStatus status
    
) {}
