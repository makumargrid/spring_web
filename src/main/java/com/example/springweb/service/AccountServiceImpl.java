package com.example.springweb.service;

import java.util.List;

import com.example.springweb.domain.Account;
import com.example.springweb.dto.AccountResponse;
import com.example.springweb.dto.CreateAccountRequest;
import com.example.springweb.dto.UpdateAccountRequest;
import com.example.springweb.exception.AccountNotFoundException;
import com.example.springweb.mapper.AccountMapper;
import com.example.springweb.repository.AccountRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;
    private final AccountMapper mapper;

    public AccountServiceImpl(AccountRepository repository, AccountMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public AccountResponse create(CreateAccountRequest request) {
        Account saved = repository.save(mapper.toEntity(request));
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponse getById(Long id) {
        return mapper.toResponse(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponse> getAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public AccountResponse update(Long id, UpdateAccountRequest request) {
        Account account = findOrThrow(id);
        mapper.applyUpdate(account, request);
        return mapper.toResponse(repository.save(account));
    }

    @Override
    public void delete(Long id) {
        repository.delete(findOrThrow(id));
    }

    private Account findOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }
}
