package org.example.console;

import org.example.exceptions.InputEndedException;
import org.example.operations.ConsoleOperationType;
import org.example.operations.OperationCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OperationsConsoleListener {

    private final Map<ConsoleOperationType, OperationCommand> commandMap;
    private final ConsoleInput consoleInput;
    private boolean running;

    @Autowired
    public OperationsConsoleListener(
            List<OperationCommand> commands,
            ConsoleInput consoleInput
    ) {
        this.consoleInput = consoleInput;
        this.commandMap = commands.stream()
                .collect(Collectors.toMap(OperationCommand::getOperationType,
                                          com -> com));

        this.running = true;
    }

    public void run() {
        printCommands();

        try {
            process();
        } catch (InputEndedException ex) {
            running = false;
            System.out.println("MiniBank stopped.");
        }
    }

    private void process() {
        while (running) {
            var nextOperation = consoleInput.readOperationType();
            processNextCommand(nextOperation);

            if (nextOperation == ConsoleOperationType.EXIT) {
                running = false;
            }
        }
    }

    private void processNextCommand(ConsoleOperationType operationType) {
        try {
            OperationCommand command = commandMap.get(operationType);

            if (command == null) {
                throw new IllegalStateException("No command handler for " + operationType);
            }

            command.execute();
        } catch (InputEndedException ex) {
            throw ex;
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            String message = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
            System.out.println("Error: " + message);
        }
    }

    private void printCommands() {
        System.out.println("MiniBank started");
        consoleInput.printAvailableCommands();
    }
}
