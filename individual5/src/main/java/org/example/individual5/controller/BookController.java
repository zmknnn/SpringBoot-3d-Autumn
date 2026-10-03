package org.example.individual5.controller;

import jakarta.validation.Valid;
import org.example.individual5.dto.BookRequest;
import org.example.individual5.dto.BookResponse;
import org.example.individual5.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse createBook(
            @Valid @RequestBody BookRequest request) {

        return bookService.createBook(request);
    }

    @GetMapping
    public List<BookResponse> getAllBooks() {

        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public BookResponse getBook(@PathVariable Long id) {

        return bookService.getBookById(id);
    }

    @PutMapping("/{id}")
    public BookResponse updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookRequest request) {

        return bookService.updateBook(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {

        bookService.deleteBook(id);
    }

    @GetMapping("/search")
    public List<BookResponse> searchBooks(
            @RequestParam String title) {
        return bookService.searchBooksByTitle(title);
    }
}