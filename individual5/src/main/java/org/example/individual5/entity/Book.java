package org.example.individual5.entity;

public record Book(
        Long id,
        String isbn,
        String title,
        String author,
        String genre,
        int year,
        BookStatus status
) {
}