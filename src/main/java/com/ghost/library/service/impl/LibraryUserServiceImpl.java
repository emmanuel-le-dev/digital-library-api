package com.ghost.library.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ghost.library.dto.user.LibraryUserRequest;
import com.ghost.library.dto.user.LibraryUserResponse;
import com.ghost.library.entity.LibraryUser;
import com.ghost.library.mapper.LibraryUserMapper;
import com.ghost.library.repository.LibraryUserRepository;
import com.ghost.library.service.LibraryUserServiceI;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LibraryUserServiceImpl implements LibraryUserServiceI {

    private final LibraryUserMapper libraryUserMapper;
    private final LibraryUserRepository libraryUserRepository;
    
    @Override
    public List<LibraryUserResponse> findAll() {
        return libraryUserRepository.findAll()
                .stream()
                .map(libraryUserMapper::toResponse)
                .toList();
    }

    @Override
    public LibraryUserResponse findById(Long id) {
        return libraryUserMapper.toResponse(requireUser(id));
    }

    @Override
    @Transactional
    public LibraryUserResponse create(LibraryUserRequest request) {
        ensureEmailAvailable(request.email(), null);

        LibraryUser user = new LibraryUser();
        libraryUserMapper.updateEntity(request, user);
        user.setActive(request.active() == null || request.active());

        LibraryUser savedUser = libraryUserRepository.save(user);
        log.info("Library user created with id={}", savedUser.getId());

        return libraryUserMapper.toResponse(savedUser);
    }

    @Override
    @Transactional
    public LibraryUserResponse update(Long id, LibraryUserRequest request) {
        LibraryUser user = requireUser(id);
        ensureEmailAvailable(request.email(), id);

        libraryUserMapper.updateEntity(request, user);

        if (request.active() != null) {
            user.setActive(request.active());
        }

        log.info("Library user updated with id={}", id);
        return libraryUserMapper.toResponse(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        LibraryUser user = requireUser(id);

        if (!user.getLoans().isEmpty()) {
            throw new IllegalStateException(
                "Cannot delete a user having loan history"
            );
        }

        libraryUserRepository.delete(user);
        log.info("Library user deleted with id={}", id);
    }
    
    private LibraryUser requireUser(Long id) {
        return libraryUserRepository.findById(id)
                .orElseThrow(
                    () -> new EntityNotFoundException(
                        "Library user not found with id: " + id
                    )
                );
    }

    private void ensureEmailAvailable(String email, Long currentId) {
        libraryUserRepository.findByEmailIgnoreCase(email)
                .filter(user -> !user.getId().equals(currentId))
                .ifPresent(user -> {
                    throw new IllegalArgumentException(
                        "Email already exists: " + email
                    );
                });
    }
    
}
