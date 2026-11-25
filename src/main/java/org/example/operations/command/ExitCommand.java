package org.example.operations.command;

import org.example.operations.ConsoleOperationType;
import org.example.operations.OperationCommand;
import org.springframework.stereotype.Component;

@Component
public class ExitCommand implements OperationCommand {

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.EXIT;
    }

    @Override
    public void execute() {
        System.out.println("MiniBank stopped.");
    }
}
