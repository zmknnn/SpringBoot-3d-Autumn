package org.example.individual3.exception;

public class InvalidBookStatusTransitionException
        extends RuntimeException {

    public InvalidBookStatusTransitionException(String message) {
        super(message);
    }
}
