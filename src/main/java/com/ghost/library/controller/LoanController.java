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

import com.ghost.library.dto.loan.LoanRequest;
import com.ghost.library.dto.loan.LoanResponse;
import com.ghost.library.service.LoanServiceI;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanServiceI loanServiceI;

    @GetMapping
    public ResponseEntity<List<LoanResponse>> findAll() {
        return ResponseEntity.ok(loanServiceI.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(loanServiceI.findById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LoanResponse>> findByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(loanServiceI.findByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<LoanResponse> create(@RequestBody LoanRequest request) {
        LoanResponse response = loanServiceI.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoanResponse> update(@PathVariable Long id, @RequestBody LoanRequest request) {
        return ResponseEntity.ok(loanServiceI.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        loanServiceI.delete(id);
        return ResponseEntity.noContent().build();
    }
    
}
