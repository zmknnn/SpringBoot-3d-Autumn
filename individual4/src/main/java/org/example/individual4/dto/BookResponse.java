package org.example.individual4.dto;

public record BookResponse(
        Long id,
        String isbn,
        String title,
        String author,
        String genre,
        int year,
        String status
) {}