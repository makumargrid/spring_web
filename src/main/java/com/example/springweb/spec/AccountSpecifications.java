package com.example.springweb.spec;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.example.springweb.domain.Account;
import com.example.springweb.domain.AccountStatus;

import org.springframework.data.jpa.domain.Specification;

/**
 * Dynamic {@link Specification}s for {@link Account}. Each factory method returns
 * {@code null} when its criterion is absent, so they compose cleanly via
 * {@link Specification#allOf}.
 */
public final class AccountSpecifications {

    private AccountSpecifications() {
    }

    /** Exact match on any of the supplied usernames (IN). */
    public static Specification<Account> usernameIn(List<String> usernames) {
        if (usernames == null || usernames.isEmpty()) {
            return null;
        }
        return (root, query, cb) -> root.get("username").in(usernames);
    }

    /** Exact match on any of the supplied statuses (IN). */
    public static Specification<Account> statusIn(List<AccountStatus> statuses) {
        if (statuses == null || statuses.isEmpty()) {
            return null;
        }
        return (root, query, cb) -> root.get("status").in(statuses);
    }

    /** Partial, case-insensitive match on email. */
    public static Specification<Account> emailContains(String email) {
        if (email == null || email.isBlank()) {
            return null;
        }
        String pattern = "%" + email.toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get("email")), pattern);
    }

    /** balance > value. */
    public static Specification<Account> balanceGreaterThan(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return (root, query, cb) -> cb.greaterThan(root.get("balance"), value);
    }

    /** balance < value. */
    public static Specification<Account> balanceLessThan(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return (root, query, cb) -> cb.lessThan(root.get("balance"), value);
    }

    /** createdAt > instant. */
    public static Specification<Account> createdAfter(Instant instant) {
        if (instant == null) {
            return null;
        }
        return (root, query, cb) -> cb.greaterThan(root.get("createdAt"), instant);
    }

    /** createdAt < instant. */
    public static Specification<Account> createdBefore(Instant instant) {
        if (instant == null) {
            return null;
        }
        return (root, query, cb) -> cb.lessThan(root.get("createdAt"), instant);
    }

    /** Combines every active criterion with logical AND. */
    public static Specification<Account> build(AccountFilter filter) {
        return Specification.allOf(
                usernameIn(filter.username()),
                statusIn(filter.status()),
                emailContains(filter.email()),
                balanceGreaterThan(filter.balanceGt()),
                balanceLessThan(filter.balanceLt()),
                createdAfter(filter.createdAfter()),
                createdBefore(filter.createdBefore())
        );
    }
}
