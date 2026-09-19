package org.example.individual3.controller;

import jakarta.validation.Valid;
import org.example.individual3.dto.BookRequest;
import org.example.individual3.dto.BookResponse;
import org.example.individual3.dto.BookStatusRequest;
import org.example.individual3.service.BookService;
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

    @PatchMapping("/{id}/status")
    public void changeStatus(
            @PathVariable Long id,
            @RequestBody BookStatusRequest request) {

        bookService.changeStatus(id, request.status());
    }
}