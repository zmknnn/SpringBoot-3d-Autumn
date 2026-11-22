package org.example.individual11.exception;

public class InvalidBookStatusTransitionException
        extends RuntimeException {

    public InvalidBookStatusTransitionException(String message) {
        super(message);
    }
}
