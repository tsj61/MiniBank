package org.example.operations.command;

import org.example.account.Account;
import org.example.account.AccountService;
import org.example.console.ConsoleInput;
import org.example.operations.ConsoleOperationType;
import org.example.operations.OperationCommand;
import org.example.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.example.helpers.ValidatorHelper.validateAccountNotNull;

@Component
public class CloseAccountCommand implements OperationCommand {

    private final AccountService accountService;
    private final UserService userService;
    private final ConsoleInput consoleInput;

    @Autowired
    public CloseAccountCommand(
            AccountService accountService,
            UserService userService,
            ConsoleInput consoleInput
    ) {
        this.accountService = accountService;
        this.userService = userService;
        this.consoleInput = consoleInput;
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_CLOSE;
    }

    @Override
    public void execute() {
        int accountId = consoleInput.readPositiveInt("Enter account id to close:", "account id");
        Account accountToDelete = accountService.getAccountById(accountId).orElse(null);
        validateAccountNotNull(accountToDelete, accountId);

        int balance = accountService.getAccountById(accountId).orElseThrow().getMoneyAmount();
        Account accountToTransfer = accountService.closeAccount(accountId);

        userService.findUserById(accountToTransfer.getUserId())
                .getAccountList()
                .removeIf(account -> account.getId() == accountId);

        System.out.println(
                "Account " + accountId + " closed. Remaining balance " + balance + " transferred " +
                        "to account " + accountToTransfer.getId() + ".");
    }
}

