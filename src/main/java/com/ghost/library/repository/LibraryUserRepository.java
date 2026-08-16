package com.ghost.library.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ghost.library.entity.LibraryUser;
import com.ghost.library.entity.LoanStatus;

public interface LibraryUserRepository extends JpaRepository<LibraryUser, Long> {
    
    Optional<LibraryUser> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    List<LibraryUser> findByActiveTrue();

    List<LibraryUser> findByLastNameContainingIgnoreCase(String lastName);

    @Query(
        """
            SELECT DISTINCT u
            FROM LibraryUser u
            JOIN u.loans l 
            WHERE l.status = :status
            ORDER BY u.lastName, u.firstName
        """
    )
    List<LibraryUser> findUsersHavingLoanWithStatus(@Param("status") LoanStatus status);

}
