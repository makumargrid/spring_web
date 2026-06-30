package com.example.springweb.service;

import java.util.List;

import com.example.springweb.dto.AccountResponse;
import com.example.springweb.dto.CreateAccountRequest;
import com.example.springweb.dto.PagedResponse;
import com.example.springweb.dto.UpdateAccountRequest;
import com.example.springweb.spec.AccountFilter;

import org.springframework.data.domain.Pageable;

public interface AccountService {

    AccountResponse create(CreateAccountRequest request);

    AccountResponse getById(Long id);

    List<AccountResponse> getAll();

    PagedResponse<AccountResponse> search(AccountFilter filter, Pageable pageable);

    AccountResponse update(Long id, UpdateAccountRequest request);

    void delete(Long id);
}
