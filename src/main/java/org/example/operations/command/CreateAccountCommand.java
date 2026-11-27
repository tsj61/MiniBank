package org.example.operations.command;

import org.example.account.Account;
import org.example.account.AccountService;
import org.example.console.ConsoleInput;
import org.example.operations.ConsoleOperationType;
import org.example.operations.OperationCommand;
import org.example.user.User;
import org.example.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateAccountCommand implements OperationCommand {

    private final UserService userService;
    private final AccountService accountService;
    private final ConsoleInput consoleInput;

    @Autowired
    public CreateAccountCommand(
            UserService userService, AccountService accountService,
            ConsoleInput consoleInput
    ) {
        this.userService = userService;
        this.accountService = accountService;
        this.consoleInput = consoleInput;
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_CREATE;
    }

    @Override
    public void execute() {
        int userId = consoleInput.readPositiveInt("Enter user id:", "user id");
        User user = userService.findUserById(userId);

        Account account = accountService.createAccount(user);
        user.getAccountList().add(account);

        System.out.println("Account created: " + account);
    }
}
