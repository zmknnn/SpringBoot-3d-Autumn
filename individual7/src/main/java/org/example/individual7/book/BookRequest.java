package org.example.individual7.book;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BookRequest(

        @NotBlank(message = "Введіть назву книги")
        @Size(max = 100, message = "Назва має містити не більше 100 символів")
        String title,

        @Min(value = 1, message = "Рік має бути не менше 1")
        @Max(value = 2026, message = "Рік не може бути більшим за 2026")
        int year,

        @NotBlank(message = "Введіть жанр")
        @Size(max = 20, message = "Жанр має містити не більше 20 символів")
        String genre,

        @NotBlank(message = "Введіть автора")
        @Size(max = 50, message = "Ім'я автора має містити не більше 50 символів")
        String author
) {}