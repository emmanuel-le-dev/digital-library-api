package com.ghost.library.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ghost.library.dto.loan.LoanRequest;
import com.ghost.library.dto.loan.LoanResponse;
import com.ghost.library.entity.Book;
import com.ghost.library.entity.LibraryUser;
import com.ghost.library.entity.Loan;
import com.ghost.library.entity.LoanStatus;
import com.ghost.library.mapper.LoanMapper;
import com.ghost.library.repository.BookRepository;
import com.ghost.library.repository.LibraryUserRepository;
import com.ghost.library.repository.LoanRepository;
import com.ghost.library.service.LoanServiceI;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoanServiceImpl implements LoanServiceI {
    
    private final LoanMapper loanMapper;
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final LibraryUserRepository libraryUserRepository;

    @Override
    public List<LoanResponse> findAll() {
        return loanRepository.findAll()
                .stream()
                .map(loanMapper::toResponse)
                .toList();
    }

    @Override
    public LoanResponse findById(Long id) {
        return loanMapper.toResponse(requireLoan(id));
    }

    @Override
    public List<LoanResponse> findByUserId(Long userId) {
        return loanRepository
                .findByUserIdOrderByBorrowedAtDesc(userId)
                .stream()
                .map(loanMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public LoanResponse create(LoanRequest request) {
        LibraryUser user = requireUser(request.userId());
        Book book = requireBook(request.bookId());

        Loan loan = new Loan();
        loanMapper.updateEntity(request, loan);
        loan.setStatus(
                request.status() == null 
                    ? LoanStatus.BORROWED 
                    : request.status()
        );

        user.addLoan(loan);
        book.addLoan(loan);

        Loan savedLoan = loanRepository.save(loan);
        log.info("Loan created with id={}", savedLoan.getId());

        return loanMapper.toResponse(savedLoan);
    }

    @Override
    @Transactional
    public LoanResponse update(Long id, LoanRequest request) {
        Loan loan = requireLoan(id);
        LibraryUser newUser = requireUser(request.userId());
        Book newBook = requireBook(request.bookId());

        if (loan.getUser() != newUser) {
            loan.getUser().getLoans().remove(loan);
            newUser.addLoan(loan);
        }

        if (loan.getBook() != newBook) {
            loan.getBook().getLoans().remove(loan);
            newBook.addLoan(loan);
        }

        loanMapper.updateEntity(request, loan);

        if (request.status() != null) {
            loan.setStatus((request.status()));
        }

        log.info("Loan updated with id={}", id);
        return loanMapper.toResponse(loan);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Loan loan = requireLoan(id);

        loan.getUser().getLoans().remove(loan);
        loan.getBook().getLoans().remove(loan);

        loanRepository.delete(loan);
        log.info("Loan deleted with id={}", id);
    }

    private Loan requireLoan(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(
                    () -> new EntityNotFoundException(
                        "Loan not found with id: " + id
                    )
                );
    }

    private LibraryUser requireUser(Long id) {
        return libraryUserRepository.findById(id)
                .orElseThrow(
                    () -> new EntityNotFoundException(
                        "Library user not found with id: " + id
                    )
                );
    }

    private Book requireBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(
                    () -> new EntityNotFoundException(
                        "Book not found with id: " + id
                    )
                );
    }

}
