package com.ghost.library.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ghost.library.dto.category.CategoryRequest;
import com.ghost.library.dto.category.CategoryResponse;
import com.ghost.library.entity.Category;
import com.ghost.library.mapper.CategoryMapper;
import com.ghost.library.repository.CategoryRepository;
import com.ghost.library.service.CategoryServiceI;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryServiceI {

    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    
    @Override
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    public CategoryResponse findById(Long id) {
        return categoryMapper.toResponse(requireCategory(null));
    }

    @Override
    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        ensureNameAvailable(request.name(), null);

        Category category = new Category();
        categoryMapper.updateEntity(request, category);

        Category savedCategory = categoryRepository.save(category);
        log.info("Category created with id={}", savedCategory.getId());

        return categoryMapper.toResponse(savedCategory);

    }

    @Override
    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = requireCategory(id);
        ensureNameAvailable(request.name(), id);

        categoryMapper.updateEntity(request, category);
        log.info("Category updated with id={}", id);

        return categoryMapper.toResponse(category);
        
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Category category = requireCategory(id);

        if (!category.getBooks().isEmpty()) {
            throw new IllegalStateException(
                "Cannot delete a category containing books"
            );
        }

        categoryRepository.delete(category);
        log.info("Category deleted with id={}", id);

    }

    private Category requireCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(
                    () -> new EntityNotFoundException(
                        "Category not found with id: " + id
                    )
                );
    }

    private void ensureNameAvailable(String name, Long currentId) {
        categoryRepository.findByNameIgnoreCase(name)
                .filter(category -> !category.getId().equals(currentId))
                .ifPresent(category -> {
                    throw new IllegalArgumentException(
                        "Category name already exists: " + name
                    );
                });
    }
    
}
