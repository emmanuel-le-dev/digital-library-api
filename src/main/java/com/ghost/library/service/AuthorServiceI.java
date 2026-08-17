package com.ghost.library.service;

import java.util.List;

import com.ghost.library.dto.author.AuthorRequest;
import com.ghost.library.dto.author.AuthorResponse;

public interface AuthorServiceI {
    
    public List<AuthorResponse> findAll();
    
    public AuthorResponse findById(Long id);
    
    public AuthorResponse create(AuthorRequest request);
    
    public AuthorResponse update(Long id, AuthorRequest request);
    
    public void delete(Long id);
    
}
