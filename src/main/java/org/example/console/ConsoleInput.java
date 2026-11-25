package org.example.console;

import org.example.exceptions.InputEndedException;
import org.example.operations.ConsoleOperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class ConsoleInput {

    private final Scanner scanner;
    private static final String ERROR = "Error: ";

    @Autowired
    public ConsoleInput(Scanner scanner) {
        this.scanner = scanner;
    }

    public void printAvailableCommands() {
        System.out.println("Available commands:");
        System.out.println(
                Arrays.stream(ConsoleOperationType.values())
                        .map(Enum::name)
                        .collect(Collectors.joining(", \n")));
    }

    public ConsoleOperationType readOperationType() {
        while (true) {
            System.out.println("Enter command:");
            String value = readLine();

            if (value.isBlank()) {
                System.out.println(ERROR + "command must not be blank");
                printAvailableCommands();
                continue;
            }

            try {
                return ConsoleOperationType.valueOf(value.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                System.out.println(ERROR + "unknown command");
                printAvailableCommands();
            }
        }
    }

    public String readRequiredString(String prompt, String fieldName) {
        while (true) {
            System.out.println(prompt);
            String value = readLine();

            if (!value.isBlank()) {
                return value;
            }

            System.out.println(ERROR + fieldName + " must not be blank");
        }
    }

    public int readPositiveInt(String prompt, String fieldName) {
        while (true) {
            String value = readRequiredString(prompt, fieldName);

            try {
                int parsed = Integer.parseInt(value);
                if (parsed <= 0) {
                    System.out.println(ERROR + fieldName + " must be > 0");
                    continue;
                }
                return parsed;
            } catch (NumberFormatException e) {
                System.out.println(ERROR + fieldName + " must be a number");
            }
        }
    }

    private String readLine() {
        if (!scanner.hasNextLine()) {
            throw new InputEndedException("Not enough lines");
        }

        return scanner.nextLine().trim();
    }
}
