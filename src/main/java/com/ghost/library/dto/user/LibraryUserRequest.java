package com.ghost.library.dto.user;

public record LibraryUserRequest(
    
    String firstName,
    
    String lastName,
    
    String email,
    
    String phoneNumber,
    
    Boolean active
    
) {}
