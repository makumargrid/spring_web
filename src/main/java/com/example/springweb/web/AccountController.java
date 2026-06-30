package com.example.springweb.web;

import java.net.URI;
import java.util.List;

import com.example.springweb.dto.AccountResponse;
import com.example.springweb.dto.CreateAccountRequest;
import com.example.springweb.dto.UpdateAccountRequest;
import com.example.springweb.service.AccountService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> create(@RequestBody CreateAccountRequest request) {
        AccountResponse created = service.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}")
    public AccountResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<AccountResponse> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    public AccountResponse update(@PathVariable Long id, @RequestBody UpdateAccountRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
