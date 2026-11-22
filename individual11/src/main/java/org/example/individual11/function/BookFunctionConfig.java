
package org.example.individual11.function;

import org.example.individual11.dto.BookSearchRequest;
import org.example.individual11.dto.BookResponse;
import org.example.individual11.service.BookService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Function;

@Configuration
public class BookFunctionConfig {

    private final BookService bookService;

    public BookFunctionConfig(BookService bookService) {
        this.bookService = bookService;
    }

    @Bean
    public Function<BookSearchRequest, List<BookResponse>> searchBooksByGenre() {
        return request -> bookService.searchBooksByGenre(request.genre());
    }
}
