package org.example.operations.command;

import org.example.account.AccountService;
import org.example.console.ConsoleInput;
import org.example.operations.ConsoleOperationType;
import org.example.operations.OperationCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DepositAccountCommand implements OperationCommand {

    private final AccountService accountService;
    private final ConsoleInput consoleInput;

    @Autowired
    public DepositAccountCommand(AccountService accountService, ConsoleInput consoleInput) {
        this.accountService = accountService;
        this.consoleInput = consoleInput;
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_DEPOSIT;
    }

    @Override
    public void execute() {
        int accountId = consoleInput.readPositiveInt("Enter account id:", "account id");
        int amount = consoleInput.readPositiveInt("Enter amount:", "amount");

        accountService.deposit(accountId, amount);
        int balance = accountService.getAccountById(accountId).orElseThrow().getMoneyAmount();

        System.out.println(
                "Deposited " + amount + " to account " + accountId + ". New balance: " + balance);
    }
}

