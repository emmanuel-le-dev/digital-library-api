package com.ghost.library.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ghost.library.dto.user.LibraryUserRequest;
import com.ghost.library.dto.user.LibraryUserResponse;
import com.ghost.library.service.LibraryUserServiceI;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class LibraryUserController {

    private final LibraryUserServiceI libraryUserServiceI;

    @GetMapping
    public ResponseEntity<List<LibraryUserResponse>> findAll() {
        return ResponseEntity.ok(libraryUserServiceI.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibraryUserResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(libraryUserServiceI.findById(id));
    }

    @PostMapping
    public ResponseEntity<LibraryUserResponse> create(@RequestBody LibraryUserRequest request) {
        LibraryUserResponse response = libraryUserServiceI.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibraryUserResponse> update(@PathVariable Long id, @RequestBody LibraryUserRequest request) {
        return ResponseEntity.ok(libraryUserServiceI.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        libraryUserServiceI.delete(id);
        return ResponseEntity.noContent().build();
    }
    
}
