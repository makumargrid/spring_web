package com.example.springweb.spec;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.example.springweb.domain.AccountStatus;

/**
 * Optional, nullable search criteria for listing accounts. Any {@code null} or
 * empty field is ignored when building the query.
 */
public record AccountFilter(
        List<String> username,
        List<AccountStatus> status,
        String email,
        BigDecimal balanceGt,
        BigDecimal balanceLt,
        Instant createdAfter,
        Instant createdBefore
) {
}
