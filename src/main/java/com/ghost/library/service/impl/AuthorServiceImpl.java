package com.ghost.library.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ghost.library.dto.author.AuthorRequest;
import com.ghost.library.dto.author.AuthorResponse;
import com.ghost.library.entity.Author;
import com.ghost.library.mapper.AuthorMapper;
import com.ghost.library.repository.AuthorRepository;
import com.ghost.library.service.AuthorServiceI;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorServiceImpl implements AuthorServiceI {

    private final AuthorMapper authorMapper;
    private final AuthorRepository authorRepository;
    
    @Override
    public List<AuthorResponse> findAll() {
        return authorRepository.findAll()
                .stream()
                .map(authorMapper::toResponse)
                .toList();
    }

    @Override
    public AuthorResponse findById(Long id) {
        return authorMapper.toResponse(requireAuthor(id));
    }

    @Override
    @Transactional
    public AuthorResponse create(AuthorRequest request) {
        Author author = new Author();
        authorMapper.updateEntity(request, author);

        Author savedAuthor = authorRepository.save(author);
        log.info("Author created with id={}", savedAuthor.getId());

        return authorMapper.toResponse(savedAuthor);
    }

    @Override
    @Transactional
    public AuthorResponse update(Long id, AuthorRequest request) {
        Author author = requireAuthor(id);
        authorMapper.updateEntity(request, author);

        log.info("Author updated with id={}", id);
        return authorMapper.toResponse(author);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Author author = requireAuthor(id);

        if (!author.getBooks().isEmpty()) {
            throw new IllegalStateException(
                "Can not delete an author associated with books"
            );
        }

        authorRepository.delete(author);
        log.info("Author deleted with id={}", id);
    }

    private Author requireAuthor(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(
                    () -> new EntityNotFoundException(
                        "Author not found with id: " + id
                    )
                );
    }
    
}
