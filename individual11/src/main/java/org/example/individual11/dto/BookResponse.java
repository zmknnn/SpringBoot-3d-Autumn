package org.example.individual11.dto;

public record BookResponse(
        Long id,
        String isbn,
        String title,
        String author,
        String genre,
        int year,
        String status
) {}