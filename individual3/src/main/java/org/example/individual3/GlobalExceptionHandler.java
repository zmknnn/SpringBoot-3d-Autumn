package org.example.individual3;

import org.example.individual3.exception.BookNotFoundException;
import org.example.individual3.exception.DuplicateIsbnException;
import org.example.individual3.exception.InvalidBookStatusTransitionException;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {

        ProblemDetail pd = ProblemDetail.forStatus(400);
        pd.setTitle("Validation failed");
        pd.setDetail("One or more fields are invalid");

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage())
                );

        pd.setProperty("errors", errors);

        return pd;
    }

    @ExceptionHandler(DuplicateIsbnException.class)
    public ProblemDetail handleDuplicateIsbnException(
            DuplicateIsbnException ex) {

        ProblemDetail pd = ProblemDetail.forStatus(409);
        pd.setTitle("Duplicate ISBN");
        pd.setDetail(ex.getMessage());

        return pd;
    }

    @ExceptionHandler(InvalidBookStatusTransitionException.class)
    public ProblemDetail handleInvalidBookStatusTransition(
            InvalidBookStatusTransitionException ex) {

        ProblemDetail pd = ProblemDetail.forStatus(422);
        pd.setTitle("Invalid book status transition");
        pd.setDetail(ex.getMessage());

        return pd;
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ProblemDetail handleBookNotFoundException(BookNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(404);
        pd.setTitle("Book not found");
        pd.setDetail(ex.getMessage());
        return pd;
    }
}