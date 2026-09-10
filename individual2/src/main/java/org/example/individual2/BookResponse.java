package org.example.individual2;

public record BookResponse(
        Long id,
        String title,
        String author,
        String genre,
        int year
) {}