package com.example.springweb.web;

import java.math.BigDecimal;
import java.util.Map;

import com.example.springweb.domain.Account;
import com.example.springweb.domain.AccountStatus;
import com.example.springweb.repository.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AccountRepository repository;

    @BeforeEach
    void clean() {
        repository.deleteAll();
    }

    private Account seed(String username) {
        Account account = new Account();
        account.setUsername(username);
        account.setEmail(username + "@example.com");
        account.setPassword("password123");
        account.setStatus(AccountStatus.ACTIVE);
        account.setBalance(new BigDecimal("100.00"));
        return repository.save(account);
    }

    private String json(Object value) throws Exception {
        return objectMapper.writeValueAsString(value);
    }

    @Test
    void createReturns201WithLocationAndBodyWithoutPassword() throws Exception {
        Map<String, Object> body = Map.of(
                "username", "john_doe",
                "email", "john@example.com",
                "password", "password123",
                "status", "ACTIVE",
                "balance", 250.50);

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(body)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.username").value("john_doe"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void getByIdReturns200() throws Exception {
        Account saved = seed("alice");

        mockMvc.perform(get("/api/v1/accounts/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("alice"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void getByIdReturns404WhenMissing() throws Exception {
        mockMvc.perform(get("/api/v1/accounts/{id}", 999_999))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllReturnsPagedAccounts() throws Exception {
        seed("alice");
        seed("bob");

        mockMvc.perform(get("/api/v1/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.metadata.totalElements").value(2))
                .andExpect(jsonPath("$.metadata.page").value(0));
    }

    @Test
    void updateReturns200AndModifiesAccount() throws Exception {
        Account saved = seed("alice");
        Map<String, Object> body = Map.of(
                "username", "alice2",
                "email", "alice2@example.com",
                "password", "newpassword1",
                "status", "SUSPENDED",
                "balance", 500.00);

        mockMvc.perform(put("/api/v1/accounts/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("alice2"))
                .andExpect(jsonPath("$.status").value("SUSPENDED"))
                .andExpect(jsonPath("$.balance").value(500.00));
    }

    @Test
    void updateReturns404WhenMissing() throws Exception {
        Map<String, Object> body = Map.of(
                "username", "ghost",
                "email", "ghost@example.com",
                "password", "password123",
                "status", "ACTIVE",
                "balance", 1.00);

        mockMvc.perform(put("/api/v1/accounts/{id}", 999_999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(body)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteReturns204() throws Exception {
        Account saved = seed("alice");

        mockMvc.perform(delete("/api/v1/accounts/{id}", saved.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/accounts/{id}", saved.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteReturns404WhenMissing() throws Exception {
        mockMvc.perform(delete("/api/v1/accounts/{id}", 999_999))
                .andExpect(status().isNotFound());
    }
}
