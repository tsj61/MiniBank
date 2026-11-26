package org.example.operations.command;

import org.example.account.Account;
import org.example.account.AccountService;
import org.example.console.ConsoleInput;
import org.example.operations.ConsoleOperationType;
import org.example.operations.OperationCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.example.helpers.ValidatorHelper.validateAccountNotNull;

@Component
public class CloseAccountCommand implements OperationCommand {

    private final AccountService accountService;
    private final ConsoleInput consoleInput;

    @Autowired
    public CloseAccountCommand(
            AccountService accountService,
            ConsoleInput consoleInput
    ) {
        this.accountService = accountService;
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

        int balance = accountToDelete.getMoneyAmount();
        Account accountToTransfer = accountService.closeAccount(accountId);

        System.out.println(
                "Account " + accountId + " closed. Remaining balance " + balance + " transferred " +
                        "to account " + accountToTransfer.getId() + ".");
    }
}

