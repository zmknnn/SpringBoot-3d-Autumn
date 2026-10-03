package org.example.individual5.service;

import org.example.individual5.dto.BookRequest;
import org.example.individual5.dto.BookResponse;
import org.example.individual5.entity.Book;
import org.example.individual5.entity.BookStatus;
import org.example.individual5.exception.BookNotFoundException;
import org.example.individual5.exception.DuplicateIsbnException;
import org.example.individual5.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private static final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public BookResponse createBook(BookRequest request) {
        if (bookRepository.findByIsbn(request.isbn()).isPresent()) {
            throw new DuplicateIsbnException(
                    "Book with ISBN " + request.isbn() + " already exists"
            );
        }

        Book book = new Book(
                null,
                request.isbn(),
                request.title(),
                request.author(),
                request.genre(),
                request.year(),
                BookStatus.AVAILABLE
        );

        Book savedBook = bookRepository.save(book);

        return toResponse(savedBook);
    }

    @Override
    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new BookNotFoundException(
                                "Book with id " + id + " not found"
                        )
                );

        return toResponse(book);
    }

    @Override
    public List<BookResponse> getAllBooks() {
        List<BookResponse> books = bookRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();

        log.info("Знайдено {} книг", books.size());

        return books;
    }

    @Override
    public BookResponse updateBook(Long id, BookRequest request) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() ->
                        new BookNotFoundException(
                                "Book with id " + id + " not found"
                        )
                );

        bookRepository.findByIsbn(request.isbn())
                .filter(book -> !book.id().equals(id))
                .ifPresent(book -> {
                    throw new DuplicateIsbnException(
                            "Book with ISBN " + request.isbn() + " already exists"
                    );
                });

        Book updatedBook = new Book(
                existingBook.id(),
                request.isbn(),
                request.title(),
                request.author(),
                request.genre(),
                request.year(),
                existingBook.status()
        );

        Book savedBook = bookRepository.save(updatedBook);

        return toResponse(savedBook);
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException(
                    "Book with id " + id + " not found"
            );
        }

        bookRepository.deleteById(id);
    }

    @Override
    public List<BookResponse> searchBooksByTitle(String title) {
        return bookRepository.findByTitle(title)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private BookResponse toResponse(Book book) {
        return new BookResponse(
                book.id(),
                book.isbn(),
                book.title(),
                book.author(),
                book.genre(),
                book.year(),
                book.status().name()
        );
    }
}