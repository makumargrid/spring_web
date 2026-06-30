package com.example.springweb.web;

import java.math.BigDecimal;
import java.time.Instant;

import com.example.springweb.domain.Account;
import com.example.springweb.domain.AccountStatus;
import com.example.springweb.repository.AccountRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountSearchTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountRepository repository;

    @BeforeEach
    void clean() {
        repository.deleteAll();
    }

    private Account save(String username, AccountStatus status, String balance, Instant createdAt) {
        Account account = new Account();
        account.setUsername(username);
        account.setEmail(username + "@example.com");
        account.setPassword("password123");
        account.setStatus(status);
        account.setBalance(new BigDecimal(balance));
        account.setCreatedAt(createdAt);
        return repository.save(account);
    }

    @Test
    void paginationReturnsMetadata() throws Exception {
        for (int i = 0; i < 5; i++) {
            save("user" + i, AccountStatus.ACTIVE, "100.00", Instant.now());
        }

        mockMvc.perform(get("/api/v1/accounts").param("page", "0").param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.metadata.totalElements").value(5))
                .andExpect(jsonPath("$.metadata.totalPages").value(3))
                .andExpect(jsonPath("$.metadata.page").value(0))
                .andExpect(jsonPath("$.metadata.size").value(2))
                .andExpect(jsonPath("$.metadata.first").value(true))
                .andExpect(jsonPath("$.metadata.last").value(false));
    }

    @Test
    void filterByMultipleStatusValues() throws Exception {
        save("a", AccountStatus.ACTIVE, "100.00", Instant.now());
        save("b", AccountStatus.SUSPENDED, "100.00", Instant.now());
        save("c", AccountStatus.INACTIVE, "100.00", Instant.now());

        mockMvc.perform(get("/api/v1/accounts").param("status", "ACTIVE", "SUSPENDED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.metadata.totalElements").value(2));
    }

    @Test
    void filterByUsernameExactMultiple() throws Exception {
        save("alice", AccountStatus.ACTIVE, "100.00", Instant.now());
        save("bob", AccountStatus.ACTIVE, "100.00", Instant.now());
        save("carol", AccountStatus.ACTIVE, "100.00", Instant.now());

        mockMvc.perform(get("/api/v1/accounts").param("username", "alice", "bob"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.metadata.totalElements").value(2));
    }

    @Test
    void filterByBalanceGreaterThan() throws Exception {
        save("a", AccountStatus.ACTIVE, "50.00", Instant.now());
        save("b", AccountStatus.ACTIVE, "150.00", Instant.now());
        save("c", AccountStatus.ACTIVE, "250.00", Instant.now());

        mockMvc.perform(get("/api/v1/accounts").param("balanceGt", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.metadata.totalElements").value(2));
    }

    @Test
    void sortByBalanceDescending() throws Exception {
        save("low", AccountStatus.ACTIVE, "10.00", Instant.now());
        save("high", AccountStatus.ACTIVE, "999.00", Instant.now());
        save("mid", AccountStatus.ACTIVE, "500.00", Instant.now());

        mockMvc.perform(get("/api/v1/accounts").param("sort", "balance,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].username").value("high"))
                .andExpect(jsonPath("$.content[2].username").value("low"));
    }

    @Test
    void filterByCreatedAfterBindsInstantAndFilters() throws Exception {
        save("old", AccountStatus.ACTIVE, "100.00", Instant.parse("2020-01-01T00:00:00Z"));
        save("recent", AccountStatus.ACTIVE, "100.00", Instant.parse("2030-01-01T00:00:00Z"));

        mockMvc.perform(get("/api/v1/accounts").param("createdAfter", "2025-01-01T00:00:00Z"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.metadata.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].username").value("recent"));
    }
}
