package com.ghost.library.service;

import java.util.List;

import com.ghost.library.dto.user.LibraryUserRequest;
import com.ghost.library.dto.user.LibraryUserResponse;

public interface LibraryUserServiceI {
    
    public List<LibraryUserResponse> findAll();

    public LibraryUserResponse findById(Long id);

    public LibraryUserResponse create(LibraryUserRequest request);

    public LibraryUserResponse update(Long id, LibraryUserRequest request);

    public void delete(Long id);
    
}
