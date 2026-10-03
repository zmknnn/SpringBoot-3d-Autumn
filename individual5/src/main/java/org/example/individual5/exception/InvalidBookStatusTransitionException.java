package org.example.individual5.exception;

public class InvalidBookStatusTransitionException
        extends RuntimeException {

    public InvalidBookStatusTransitionException(String message) {
        super(message);
    }
}
