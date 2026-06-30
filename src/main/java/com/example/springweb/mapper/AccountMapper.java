package com.example.springweb.mapper;

import com.example.springweb.domain.Account;
import com.example.springweb.dto.AccountResponse;
import com.example.springweb.dto.CreateAccountRequest;
import com.example.springweb.dto.UpdateAccountRequest;

import org.springframework.stereotype.Component;

/**
 * Maps between the {@link Account} entity and its DTOs, keeping persistence types
 * out of the web layer.
 */
@Component
public class AccountMapper {

    public Account toEntity(CreateAccountRequest request) {
        Account account = new Account();
        account.setUsername(request.username());
        account.setEmail(request.email());
        account.setPassword(request.password());
        account.setStatus(request.status());
        account.setBalance(request.balance());
        return account;
    }

    public void applyUpdate(Account account, UpdateAccountRequest request) {
        account.setUsername(request.username());
        account.setEmail(request.email());
        account.setPassword(request.password());
        account.setStatus(request.status());
        account.setBalance(request.balance());
    }

    public AccountResponse toResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getUsername(),
                account.getEmail(),
                account.getStatus(),
                account.getBalance(),
                account.getCreatedAt()
        );
    }
}
