package com.example.springweb.service;

import java.util.List;

import com.example.springweb.domain.Account;
import com.example.springweb.dto.AccountResponse;
import com.example.springweb.dto.CreateAccountRequest;
import com.example.springweb.dto.PagedResponse;
import com.example.springweb.dto.UpdateAccountRequest;
import com.example.springweb.exception.AccountNotFoundException;
import com.example.springweb.mapper.AccountMapper;
import com.example.springweb.repository.AccountRepository;
import com.example.springweb.spec.AccountFilter;
import com.example.springweb.spec.AccountSpecifications;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    @Transactional(readOnly = true)
    public PagedResponse<AccountResponse> search(AccountFilter filter, Pageable pageable) {
        Specification<Account> specification = AccountSpecifications.build(filter);
        Page<AccountResponse> page = repository.findAll(specification, pageable)
                .map(mapper::toResponse);
        return PagedResponse.from(page);
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
