package com.ghost.library.service;

import java.util.List;

import com.ghost.library.dto.loan.LoanRequest;
import com.ghost.library.dto.loan.LoanResponse;

public interface LoanServiceI {
    
    public List<LoanResponse> findAll();

    public LoanResponse findById(Long id);

    public List<LoanResponse> findByUserId(Long userId);

    public LoanResponse create(LoanRequest request);

    public LoanResponse update(Long id, LoanRequest request);

    public void delete(Long id);

}
