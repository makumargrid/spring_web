package com.example.springweb.spec;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.example.springweb.domain.Account;
import com.example.springweb.domain.AccountStatus;
import com.example.springweb.repository.AccountRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AccountSpecificationsTest {

    @Autowired
    AccountRepository repository;

    private void save(String username, AccountStatus status, String balance, String email, Instant createdAt) {
        Account account = new Account();
        account.setUsername(username);
        account.setEmail(email);
        account.setPassword("password123");
        account.setStatus(status);
        account.setBalance(new BigDecimal(balance));
        account.setCreatedAt(createdAt);
        repository.save(account);
    }

    @BeforeEach
    void seed() {
        repository.deleteAll();
        save("alice", AccountStatus.ACTIVE, "100.00", "alice@example.com", Instant.parse("2020-01-01T00:00:00Z"));
        save("bob", AccountStatus.SUSPENDED, "500.00", "bob@test.com", Instant.parse("2025-01-01T00:00:00Z"));
        save("carol", AccountStatus.INACTIVE, "900.00", "carol@example.com", Instant.parse("2030-01-01T00:00:00Z"));
    }

    @Test
    void usernameInMatchesExactValues() {
        List<Account> result = repository.findAll(AccountSpecifications.usernameIn(List.of("alice", "carol")));
        assertThat(result).extracting(Account::getUsername).containsExactlyInAnyOrder("alice", "carol");
    }

    @Test
    void statusInMatchesAnyStatus() {
        List<Account> result = repository.findAll(
                AccountSpecifications.statusIn(List.of(AccountStatus.ACTIVE, AccountStatus.SUSPENDED)));
        assertThat(result).extracting(Account::getUsername).containsExactlyInAnyOrder("alice", "bob");
    }

    @Test
    void emailContainsIsPartialAndCaseInsensitive() {
        List<Account> result = repository.findAll(AccountSpecifications.emailContains("EXAMPLE.com"));
        assertThat(result).extracting(Account::getUsername).containsExactlyInAnyOrder("alice", "carol");
    }

    @Test
    void balanceGreaterThanIsStrict() {
        List<Account> result = repository.findAll(AccountSpecifications.balanceGreaterThan(new BigDecimal("100.00")));
        assertThat(result).extracting(Account::getUsername).containsExactlyInAnyOrder("bob", "carol");
    }

    @Test
    void balanceLessThanIsStrict() {
        List<Account> result = repository.findAll(AccountSpecifications.balanceLessThan(new BigDecimal("900.00")));
        assertThat(result).extracting(Account::getUsername).containsExactlyInAnyOrder("alice", "bob");
    }

    @Test
    void createdAfterFiltersByDate() {
        List<Account> result = repository.findAll(AccountSpecifications.createdAfter(Instant.parse("2022-01-01T00:00:00Z")));
        assertThat(result).extracting(Account::getUsername).containsExactlyInAnyOrder("bob", "carol");
    }

    @Test
    void createdBeforeFiltersByDate() {
        List<Account> result = repository.findAll(AccountSpecifications.createdBefore(Instant.parse("2022-01-01T00:00:00Z")));
        assertThat(result).extracting(Account::getUsername).containsExactly("alice");
    }

    @Test
    void buildCombinesActiveFiltersWithAnd() {
        AccountFilter filter = new AccountFilter(
                null,
                List.of(AccountStatus.SUSPENDED, AccountStatus.INACTIVE),
                null,
                new BigDecimal("100.00"),
                null,
                null,
                null);

        List<Account> result = repository.findAll(AccountSpecifications.build(filter));

        assertThat(result).extracting(Account::getUsername).containsExactlyInAnyOrder("bob", "carol");
    }

    @Test
    void nullSpecificationReturnsAll() {
        List<Account> result = repository.findAll(AccountSpecifications.usernameIn(null));
        assertThat(result).hasSize(3);
    }
}
