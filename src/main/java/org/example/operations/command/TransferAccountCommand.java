package org.example.operations.command;

import org.example.account.Account;
import org.example.account.AccountService;
import org.example.console.ConsoleInput;
import org.example.operations.ConsoleOperationType;
import org.example.operations.OperationCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransferAccountCommand implements OperationCommand {

    private final AccountService accountService;
    private final ConsoleInput consoleInput;

    @Autowired
    public TransferAccountCommand(AccountService accountService, ConsoleInput consoleInput) {
        this.accountService = accountService;
        this.consoleInput = consoleInput;
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_TRANSFER;
    }

    @Override
    public void execute() {
        int fromAccountId = consoleInput.readPositiveInt("Enter source account id:", "source account id");
        int toAccountId = consoleInput.readPositiveInt("Enter target account id:", "target account id");
        int amount = consoleInput.readPositiveInt("Enter amount:", "amount");

        if (fromAccountId == toAccountId) {
            throw new IllegalArgumentException("source and target account id must be different");
        }

        int fromUser = accountService.getAccountById(fromAccountId)
                .map(Account::getUserId)
                .orElse(-1);
        int toUser = accountService.getAccountById(toAccountId).map(Account::getUserId).orElse(-2);
        int received = accountService.transfer(fromAccountId, toAccountId, amount);

        String base = "Transfer completed from account " + fromAccountId + " to account " + toAccountId + ". Amount: " + amount;

        if (fromUser == toUser) {
            System.out.println(base + " (no commission, same user)");
        } else {
            System.out.println(
                    base + ", commission: " + (amount - received) + ", recipient received: " + received);
        }
    }
}

