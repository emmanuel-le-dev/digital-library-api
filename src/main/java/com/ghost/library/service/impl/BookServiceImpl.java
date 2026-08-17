package com.ghost.library.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ghost.library.dto.book.BookRequest;
import com.ghost.library.dto.book.BookResponse;
import com.ghost.library.entity.Author;
import com.ghost.library.entity.Book;
import com.ghost.library.entity.Category;
import com.ghost.library.mapper.BookMapper;
import com.ghost.library.repository.AuthorRepository;
import com.ghost.library.repository.BookRepository;
import com.ghost.library.repository.CategoryRepository;
import com.ghost.library.service.BookServiceI;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookServiceImpl implements BookServiceI {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    
    @Override
    public List<BookResponse> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toResponse)
                .toList();
    }

    @Override
    public BookResponse findById(Long id) {
        return bookMapper.toResponse(requireBook(id));
    }

    @Override
    public List<BookResponse> search(String keyword) {
        return bookRepository.searchByTitleOrIsbn(keyword)
                .stream()
                .map(bookMapper::toResponse)
                .toList();
    }

    @Override
    public List<BookResponse> findAvailableByCategory(String categoryName) {
        return bookRepository
                .findAvailableBooksByCategoryName(categoryName)
                .stream()
                .map(bookMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public BookResponse create(BookRequest request) {
        ensureIsbnAvailable(request.isbn(), null);
        validateTotalCopies(request.totalCopies());

        Category category = requireCategory(request.categoryId());
        List<Author> authors = requireAuthors(request.authorIds());

        Book book = new Book();
        bookMapper.updateEntity(request, book);
        book.setAvailableCopies(request.totalCopies());

        category.addBook(book);
        authors.forEach(book::addAuthor);

        Book savedBook = bookRepository.save(book);
        log.info("Book created with id={}", savedBook.getId());

        return bookMapper.toResponse(savedBook);
    }

    @Override
    @Transactional
    public BookResponse update(Long id, BookRequest request) {
        Book book = requireBook(id);

        ensureIsbnAvailable(request.isbn(), id);
        validateTotalCopies(request.totalCopies());

        int borrowedCopies = book.getTotalCopies() - book.getAvailableCopies();

        if (request.totalCopies() < borrowedCopies) {
            throw new IllegalStateException(
                "Total copies cannot be lower than borrowed copies"
            );
        }

        Category newCategory = requireCategory(request.categoryId());
        List<Author> newAuthors = requireAuthors(request.authorIds());

        if (book.getCategory() != newCategory) {
            book.getCategory().getBooks().remove(book);
            newCategory.addBook(book);
        }

        new HashSet<>(book.getAuthors())
                .forEach(book::removeAuthor);

        newAuthors.forEach(book::addAuthor);

        bookMapper.updateEntity(request, book);
        book.setAvailableCopies(
            request.totalCopies() - borrowedCopies
        );

        log.info("Book updated with id={}", id);
        return bookMapper.toResponse(book);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Book book = requireBook(id);

        if (!book.getLoans().isEmpty()) {
            throw new IllegalStateException(
                "Cannot delete a book having loan history"
            );
        }

        new HashSet<>(book.getAuthors())
                .forEach(book::removeAuthor);

        book.getCategory().getBooks().remove(book);
        bookRepository.delete(book);

        log.info("Book deleted with id={}", id);
    }

    private Book requireBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(
                    () -> new EntityNotFoundException(
                        "Book not found with id: " + id
                    )
                );
    }

    private Category requireCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(
                    () -> new EntityNotFoundException(
                        "Category not found with id: " + id
                    )
                );
    }

    private List<Author> requireAuthors(Set<Long> authorIds) {
        if (authorIds == null || authorIds.isEmpty()) {
            return List.of();
        }

        Set<Long> uniqueIds = new HashSet<>(authorIds);
        List<Author> authors = new ArrayList<>(
            authorRepository.findAllById(uniqueIds)
        );

        if (authors.size() != uniqueIds.size()) {
            Set<Long> foundIds = authors.stream()
                    .map(Author::getId)
                    .collect(java.util.stream.Collectors.toSet());

            Set<Long> missingIds = new HashSet<>(uniqueIds);
            missingIds.removeAll(foundIds);

            throw new EntityNotFoundException(
                "Authors not found with ids: " + missingIds
            );
        }

        return authors;
    }

    private void ensureIsbnAvailable(String isbn, Long currentId) {
        bookRepository.findByIsbn(isbn)
                .filter(book -> !book.getId().equals(currentId))
                .ifPresent(book -> {
                    throw new IllegalArgumentException(
                        "ISBN already exists: " + isbn
                    );
                });
    }

    private void validateTotalCopies(Integer totalCopies) {
        if (totalCopies == null || totalCopies < 0) {
            throw new IllegalArgumentException(
                "Total copies must be zero or greater"
            );
        }
    }
    
}
