package org.example.individual2;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final Map<Long, Book> books = new HashMap<>();
    private long nextId = 1;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse createBook(@Valid @RequestBody BookRequest request) {

        Book book = new Book(
                nextId,
                request.title(),
                request.author(),
                request.genre(),
                request.year()
        );

        books.put(nextId, book);
        nextId++;

        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getYear()
        );
    }

    @GetMapping
    public List<BookResponse> getAllBooks() {

        List<BookResponse> responses = new ArrayList<>();

        for (Book book : books.values()) {
            responses.add(new BookResponse(
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getGenre(),
                    book.getYear()
            ));
        }

        return responses;
    }

    @GetMapping("/{id}")
    public BookResponse getBook(@PathVariable Long id) {

        Book book = books.get(id);

        if (book == null) {
            throw new RuntimeException("Book not found");
        }

        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getYear()
        );
    }

    @PutMapping("/{id}")
    public BookResponse updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookRequest request) {

        Book book = books.get(id);

        if (book == null) {
            throw new RuntimeException("Book not found");
        }

        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setGenre(request.genre());
        book.setYear(request.year());

        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getYear()
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {

        Book removedBook = books.remove(id);

        if (removedBook == null) {
            throw new RuntimeException("Book not found");
        }
    }
}