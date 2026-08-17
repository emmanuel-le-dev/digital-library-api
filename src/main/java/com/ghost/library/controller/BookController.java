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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ghost.library.dto.book.BookRequest;
import com.ghost.library.dto.book.BookResponse;
import com.ghost.library.service.BookServiceI;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookServiceI bookServiceI;

    @GetMapping
    public ResponseEntity<List<BookResponse>> findAll() {
        return ResponseEntity.ok(bookServiceI.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(bookServiceI.findById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookResponse>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(bookServiceI.search(keyword));
    }

    @GetMapping("/available")
    public ResponseEntity<List<BookResponse>> findAvailableByCategory(@RequestParam String categoryName) {
        return ResponseEntity.ok(bookServiceI.findAvailableByCategory(categoryName));
    }

    @PostMapping
    public ResponseEntity<BookResponse> create(@RequestBody BookRequest request) {
        BookResponse response = bookServiceI.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> update(@PathVariable Long id, @RequestBody BookRequest request) {
        return ResponseEntity.ok(bookServiceI.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookServiceI.delete(id);
        return ResponseEntity.noContent().build();
    }
    
}
