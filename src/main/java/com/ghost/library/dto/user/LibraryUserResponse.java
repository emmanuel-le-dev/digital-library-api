package com.ghost.library.dto.user;

public record LibraryUserResponse(
    
    Long id,
    
    String firstName,
    
    String lastName,
    
    String email,
    
    String phoneNumber,
    
    boolean active
    
) {}
