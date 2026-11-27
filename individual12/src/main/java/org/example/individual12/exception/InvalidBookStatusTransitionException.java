package org.example.individual12.exception;

public class InvalidBookStatusTransitionException
        extends RuntimeException {

    public InvalidBookStatusTransitionException(String message) {
        super(message);
    }
}
