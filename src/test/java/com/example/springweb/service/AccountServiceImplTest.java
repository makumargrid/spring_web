package com.example.springweb.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.example.springweb.domain.Account;
import com.example.springweb.domain.AccountStatus;
import com.example.springweb.dto.AccountResponse;
import com.example.springweb.dto.CreateAccountRequest;
import com.example.springweb.dto.UpdateAccountRequest;
import com.example.springweb.exception.AccountNotFoundException;
import com.example.springweb.mapper.AccountMapper;
import com.example.springweb.repository.AccountRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccountServiceImplTest {

    private AccountRepository repository;
    private AccountServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(AccountRepository.class);
        service = new AccountServiceImpl(repository, new AccountMapper());
    }

    private Account entity(Long id, String username) {
        Account account = new Account();
        account.setId(id);
        account.setUsername(username);
        account.setEmail(username + "@example.com");
        account.setPassword("password123");
        account.setStatus(AccountStatus.ACTIVE);
        account.setBalance(new BigDecimal("100.00"));
        account.setCreatedAt(Instant.now());
        return account;
    }

    @Test
    void createPersistsAndReturnsResponse() {
        when(repository.save(any(Account.class))).thenAnswer(invocation -> {
            Account a = invocation.getArgument(0);
            a.setId(1L);
            a.setCreatedAt(Instant.now());
            return a;
        });
        CreateAccountRequest request = new CreateAccountRequest(
                "john", "john@example.com", "password123", AccountStatus.ACTIVE, new BigDecimal("10.00"));

        AccountResponse response = service.create(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.username()).isEqualTo("john");
        verify(repository).save(any(Account.class));
    }

    @Test
    void getByIdReturnsResponseWhenFound() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity(1L, "alice")));

        assertThat(service.getById(1L).username()).isEqualTo("alice");
    }

    @Test
    void getByIdThrowsWhenMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(99L))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void getAllReturnsResponses() {
        when(repository.findAll()).thenReturn(List.of(entity(1L, "a"), entity(2L, "b")));

        assertThat(service.getAll()).hasSize(2);
    }

    @Test
    void updateModifiesWhenFound() {
        Account existing = entity(1L, "alice");
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
        UpdateAccountRequest request = new UpdateAccountRequest(
                "alice2", "alice2@example.com", "password456", AccountStatus.SUSPENDED, new BigDecimal("200.00"));

        AccountResponse response = service.update(1L, request);

        assertThat(response.username()).isEqualTo("alice2");
        assertThat(response.status()).isEqualTo(AccountStatus.SUSPENDED);
    }

    @Test
    void updateThrowsWhenMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(99L,
                new UpdateAccountRequest("x", "x@example.com", "password789", AccountStatus.ACTIVE, BigDecimal.ONE)))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void deleteRemovesWhenFound() {
        Account existing = entity(1L, "alice");
        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        service.delete(1L);

        verify(repository).delete(existing);
    }

    @Test
    void deleteThrowsWhenMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(AccountNotFoundException.class);
    }
}
