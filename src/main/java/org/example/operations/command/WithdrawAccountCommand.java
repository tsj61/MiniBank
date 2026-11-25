package org.example.operations.command;

import org.example.account.AccountService;
import org.example.console.ConsoleInput;
import org.example.operations.ConsoleOperationType;
import org.example.operations.OperationCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WithdrawAccountCommand implements OperationCommand {

    private final AccountService accountService;
    private final ConsoleInput consoleInput;

    @Autowired
    public WithdrawAccountCommand(AccountService accountService, ConsoleInput consoleInput) {
        this.accountService = accountService;
        this.consoleInput = consoleInput;
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_WITHDRAW;
    }

    @Override
    public void execute() {
        int accountId = consoleInput.readPositiveInt("Enter account id:", "account id");
        int amount = consoleInput.readPositiveInt("Enter amount:", "amount");

        accountService.withdraw(accountId, amount);

        System.out.println("Withdrawn " + amount + " from account " + accountId + ".");
    }
}

