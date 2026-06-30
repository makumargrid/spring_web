package com.example.springweb.service;

import java.util.List;

import com.example.springweb.dto.AccountResponse;
import com.example.springweb.dto.CreateAccountRequest;
import com.example.springweb.dto.UpdateAccountRequest;

public interface AccountService {

    AccountResponse create(CreateAccountRequest request);

    AccountResponse getById(Long id);

    List<AccountResponse> getAll();

    AccountResponse update(Long id, UpdateAccountRequest request);

    void delete(Long id);
}
