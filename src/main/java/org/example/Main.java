package org.example;

import org.example.config.ApplicationConfiguration;
import org.example.console.OperationsConsoleListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    static void main() {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(ApplicationConfiguration.class)) {
            OperationsConsoleListener consoleListener = context.getBean(
                    OperationsConsoleListener.class);
            consoleListener.run();
        }
    }
}
