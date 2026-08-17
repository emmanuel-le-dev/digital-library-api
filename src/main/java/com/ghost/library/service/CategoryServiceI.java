package com.ghost.library.service;

import java.util.List;

import com.ghost.library.dto.category.CategoryRequest;
import com.ghost.library.dto.category.CategoryResponse;

public interface CategoryServiceI {
    
    public List<CategoryResponse> findAll();

    public CategoryResponse findById(Long id);

    public CategoryResponse create(CategoryRequest request);

    public CategoryResponse update(Long id, CategoryRequest request);

    public void delete(Long id);

}
