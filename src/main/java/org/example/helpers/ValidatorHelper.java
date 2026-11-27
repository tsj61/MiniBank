package org.example.helpers;

import org.example.account.Account;
import org.example.user.User;

public final class ValidatorHelper {

    private ValidatorHelper() {
    }

    public static void validatePositiveParam(Integer value, String param) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException(param + " must be > 0");
        }
    }

    public static void validateUserNotNull(User user, Integer userId) {
        if (user == null) {
            if (userId != null) {
                throw new IllegalArgumentException(
                        "No such user: id=%s"
                                .formatted(userId));
            } else {
                throw new IllegalArgumentException("user must not be null");
            }
        }
    }

    public static void validateAccountNotNull(Account account, Integer accountId) {
        if (account == null) {
            throw new IllegalArgumentException(
                    "No such account: id=%s"
                            .formatted(accountId));
        }
    }

    public static void validateLoginNotNullAndBlank(String login) {
        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("login must not be blank");
        }
    }

    public static void validateSufficientFunds(
            Integer amount,
            Integer oldAmount,
            Integer accountId
    ) {
        if (amount > oldAmount) {
            throw new IllegalArgumentException(
                    "insufficient funds on account id=%s, money amount=%s, attempted =%s"
                            .formatted(accountId,
                                       oldAmount,
                                       amount)
            );
        }
    }
}
