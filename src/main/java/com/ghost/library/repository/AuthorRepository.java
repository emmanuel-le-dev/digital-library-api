package com.ghost.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ghost.library.entity.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> findByLastNameIgnoreCase(String lastName);

    List<Author> findByFirstNameContainingIgnoreCase(String firstName);

    List<Author> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);

    @Query(
        """
            SELECT DISTINCT a
            FROM Author a
            JOIN a.books b
            WHERE b.id = :bookId
            ORDER BY a.lastName, a.firstName        
        """
    )
    List<Author> findAuthorsByBookId(@Param("bookId") Long bookId);
    
}
