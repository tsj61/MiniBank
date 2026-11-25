package org.example.operations.command;

import org.example.operations.ConsoleOperationType;
import org.example.operations.OperationCommand;
import org.example.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AllUsersCommand implements OperationCommand {

    private final UserService userService;

    @Autowired
    public AllUsersCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.SHOW_ALL_USERS;
    }

    @Override
    public void execute() {
        userService.findAllUsers().forEach(System.out::println);
    }
}
