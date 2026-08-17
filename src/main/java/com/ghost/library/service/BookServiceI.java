package com.ghost.library.service;

import java.util.List;

import com.ghost.library.dto.book.BookRequest;
import com.ghost.library.dto.book.BookResponse;

public interface BookServiceI {

    public List<BookResponse> findAll();

    public BookResponse findById(Long id);

    public List<BookResponse> search(String keyword);

    public List<BookResponse> findAvailableByCategory(String categoryName); 

    public BookResponse create(BookRequest request);

    public BookResponse update(Long id, BookRequest request);

    public void delete(Long id);
    
}
