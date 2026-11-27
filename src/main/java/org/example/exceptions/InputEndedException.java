package org.example.exceptions;

public class InputEndedException extends IllegalArgumentException {
    public InputEndedException(String message) {
        super(message);
    }
}
