package org.example.operations;

public interface OperationCommand {

    ConsoleOperationType getOperationType();

    void execute();
}
