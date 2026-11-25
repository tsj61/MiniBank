package org.example.operations.command;

import org.example.console.ConsoleInput;
import org.example.operations.ConsoleOperationType;
import org.example.operations.OperationCommand;
import org.example.user.User;
import org.example.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateUserCommand implements OperationCommand {

    private final ConsoleInput consoleInput;
    private final UserService userService;

    @Autowired
    public CreateUserCommand(ConsoleInput consoleInput, UserService userService) {
        this.consoleInput = consoleInput;
        this.userService = userService;
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.USER_CREATE;
    }

    @Override
    public void execute() {
        String login = consoleInput.readRequiredString("Enter login:", "login");
        User user = userService.createUser(login);
        System.out.println("User created: " + user);
    }
}
