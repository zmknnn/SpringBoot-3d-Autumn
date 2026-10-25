package org.example.individual8.exception;

public class InvalidBookStatusTransitionException
        extends RuntimeException {

    public InvalidBookStatusTransitionException(String message) {
        super(message);
    }
}
