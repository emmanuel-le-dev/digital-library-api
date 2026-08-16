package com.ghost.library.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ghost.library.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    @Query(
        """
            SELECT DISTINCT c
            FROM Category c
            JOIN c.books b
            WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))
        """
    )
    List<Category> findCategoriesContainingBookTitle(
        @Param("title") String title
    );
    
}
