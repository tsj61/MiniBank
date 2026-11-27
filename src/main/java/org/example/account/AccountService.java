package org.example.account;

import org.example.user.User;
import org.example.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.example.helpers.ValidatorHelper.*;

@Component
public class AccountService {

    private int idCounter;
    private final Map<Integer, Account> accountsMap;
    private UserService userService;

    private static final String USER_ID = "User id";
    private static final String ACCOUNT_ID = "Account id";
    private static final String AMOUNT = "Amount";

    private final AccountProperties accountProperties;

    @Autowired
    public AccountService(AccountProperties accountProperties) {
        this.accountProperties = accountProperties;
        this.idCounter = 0;
        this.accountsMap = new HashMap<>();
    }

    public Account createAccount(User user) {
        validateUserNotNull(user, null);

        idCounter++;
        Account account = new Account(idCounter, user.getId(), accountProperties.getDefaultAmount());
        accountsMap.put(idCounter, account);

        return account;
    }

    public Optional<Account> getAccountById(Integer accountId) {
        validatePositiveParam(accountId, ACCOUNT_ID);

        return Optional.ofNullable(accountsMap.get(accountId));
    }

    public List<Account> getUserAccounts(Integer userId) {
        validatePositiveParam(userId, USER_ID);

        return accountsMap.values().stream()
                .filter(a -> userId.equals(a.getUserId()))
                .toList();
    }

    public void withdraw(Integer accountId, Integer amount) {
        Account account = getExistingAccount(accountId);

        validatePositiveParam(amount, AMOUNT);
        validateSufficientFunds(amount, account.getMoneyAmount(), accountId);

        account.setMoneyAmount(account.getMoneyAmount() - amount);
    }

    public void deposit(Integer accountId, Integer amount) {
        validatePositiveParam(amount, AMOUNT);

        Account account = getExistingAccount(accountId);

        account.setMoneyAmount(account.getMoneyAmount() + amount);
    }

    public Account closeAccount(Integer accountId) {
        Account accountToClose = getExistingAccount(accountId);

        List<Account> userAccounts = getUserAccounts(accountToClose.getUserId());

        if (userAccounts.size() == 1) {
            throw new IllegalStateException("Can't close the only one account");
        }

        Account accountToTransfer = userAccounts.stream()
                .filter(a -> a.getId() != accountId)
                .min(Comparator.comparingInt(Account::getId))
                .orElseThrow();

        accountToTransfer.setMoneyAmount(
                accountToTransfer.getMoneyAmount() + accountToClose.getMoneyAmount());

        accountsMap.remove(accountId);

        userService.findUserById(accountToClose.getUserId())
                .getAccountList()
                .removeIf(account -> account.getId() == accountId);

        return accountToTransfer;
    }

    public int transfer(Integer fromAccountId, Integer toAccountId, Integer amount) {
        validatePositiveParam(amount, AMOUNT);

        if (Objects.equals(fromAccountId, toAccountId)) {
            throw new IllegalArgumentException("source and target account id must be different");
        }

        Account accountFrom = getExistingAccount(fromAccountId);

        Account accountTo = getExistingAccount(toAccountId);

        validateSufficientFunds(amount, accountFrom.getMoneyAmount(), fromAccountId);

        int amountToTransfer = accountTo.getUserId() == accountFrom.getUserId()
                ? amount
                : (int) Math.round(amount * (1 - accountProperties.getTransferCommission()));

        accountFrom.setMoneyAmount(accountFrom.getMoneyAmount() - amount);
        accountTo.setMoneyAmount(accountTo.getMoneyAmount() + amountToTransfer);

        return amountToTransfer;
    }

    private Account getExistingAccount(Integer id) {
        Account account = getAccountById(id).orElse(null);
        validateAccountNotNull(account, id);
        return account;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
