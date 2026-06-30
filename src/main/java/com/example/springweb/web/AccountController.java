package com.example.springweb.web;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.util.List;

import com.example.springweb.domain.AccountStatus;
import com.example.springweb.dto.AccountResponse;
import com.example.springweb.dto.CreateAccountRequest;
import com.example.springweb.dto.PagedResponse;
import com.example.springweb.dto.UpdateAccountRequest;
import com.example.springweb.service.AccountService;
import com.example.springweb.spec.AccountFilter;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<AccountResponse> create(@Valid @RequestBody CreateAccountRequest request) {
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

    /**
     * Paginated, filterable and sortable listing.
     *
     * <p>Pagination/sorting come from {@code page}, {@code size} and {@code sort}
     * (e.g. {@code sort=balance,desc}). Filters: {@code username} and {@code status}
     * are repeatable exact matches; {@code email} is a partial match;
     * {@code balanceGt}/{@code balanceLt} and {@code createdAfter}/{@code createdBefore}
     * are range operators.
     */
    @GetMapping
    public PagedResponse<AccountResponse> search(
            @RequestParam(required = false) List<String> username,
            @RequestParam(required = false) List<AccountStatus> status,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) BigDecimal balanceGt,
            @RequestParam(required = false) BigDecimal balanceLt,
            @RequestParam(required = false) Instant createdAfter,
            @RequestParam(required = false) Instant createdBefore,
            Pageable pageable) {
        AccountFilter filter = new AccountFilter(
                username, status, email, balanceGt, balanceLt, createdAfter, createdBefore);
        return service.search(filter, pageable);
    }

    @PutMapping("/{id}")
    public AccountResponse update(@PathVariable Long id, @Valid @RequestBody UpdateAccountRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
