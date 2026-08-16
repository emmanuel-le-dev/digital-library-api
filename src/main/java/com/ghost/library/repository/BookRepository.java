package com.ghost.library.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ghost.library.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByCategoryId(Long categoryId);

    List<Book> findByAuthorsId(Long authorId);

    List<Book> findByAvailableCopiesGreaterThan(Integer quantity);

    List<Book> findByLanguageIgnoreCaseAndAvailableCopiesGreaterThan(
        String language,
        Integer minimumAvailableCopies
    );

    @Query(
        """
            SELECT DISTINCT b
            FROM Book b
            JOIN FETCH b.category
            LEFT JOIN FETCH b.authors
            WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(b.isbn) LIKE LOWER(CONCAT('%', :keyword, '%'))
            ORDER BY b.title
        """
    )
    List<Book> searchByTitleOrIsbn(@Param("keyword") String keyword);

    @Query(
        """
            SELECT b
            FROM Book b
            WHERE b.availableCopies > 0
            AND LOWER(b.category.name) = LOWER(:categoryName)
            ORDER BY b.title
        """
    )
    List<Book> findAvailableBooksByCategoryName(@Param("categoryName") String categoryName);
    
}
