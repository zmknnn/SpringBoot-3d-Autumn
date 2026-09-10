package org.example.individual2;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BookRequest(

        @NotBlank(message = "Book title is required")
        @Size(min = 1, max = 100, message = "Title must contain from 1 to 100 characters")
        String title,

        @Min(value = 0, message = "Year cannot be negative")
        @Max(value = 2026, message = "Year cannot be greater than 2026")
        int year,

        @Size(min = 1, max = 20, message = "Genre must contain from 1 to 20 characters")
        String genre,

        @Size(min = 1, max = 50, message = "Author name must contain from 1 to 50 characters")
        String author
) {}
