package com.ghost.library.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ghost.library.entity.Loan;
import com.ghost.library.entity.LoanStatus;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByUserIdOrderByBorrowedAtDesc(Long userId);

    List<Loan> findByBookIdOrderByBorrowedAtDesc(Long bookId);

    List<Loan> findByStatusOrderByDueAtAsc(LoanStatus status);

    long countByUserIdAndStatus(Long userId, LoanStatus status);

    boolean existsByUserIdAndBookIdAndStatus(Long userId, Long bookId, LoanStatus status);

    @Query(
        """
            SELECT l
            FROM Loan l
            JOIN FETCH l.user
            JOIN FETCH l.book
            WHERE l.status = :status
            AND l.dueAt < :referenceTime
            ORDER BY l.dueAt
        """
    )
    List<Loan> findLoansPastDueDate(@Param("status") LoanStatus status, @Param("referenceTime") LocalDateTime referenceTime);
    
}
