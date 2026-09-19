package org.example.individual3.service;

import org.example.individual3.dto.BookRequest;
import org.example.individual3.dto.BookResponse;
import org.example.individual3.entity.Book;
import org.example.individual3.entity.BookStatus;
import org.example.individual3.exception.BookNotFoundException;
import org.example.individual3.exception.DuplicateIsbnException;
import org.example.individual3.exception.InvalidBookStatusTransitionException;
import org.example.individual3.repository.BookRepository;
import org.example.individual3.strategy.StatusTransitionStrategy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final List<StatusTransitionStrategy> statusTransitionStrategies;

    public BookServiceImpl(
            BookRepository bookRepository,
            List<StatusTransitionStrategy> statusTransitionStrategies) {

        this.bookRepository = bookRepository;
        this.statusTransitionStrategies = statusTransitionStrategies;
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
        return bookRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public BookResponse updateBook(Long id, BookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new BookNotFoundException(
                                "Book with id " + id + " not found"
                        )
                );

        bookRepository.findByIsbn(request.isbn())
                .filter(existingBook -> !existingBook.getId().equals(id))
                .ifPresent(existingBook -> {
                    throw new DuplicateIsbnException(
                            "Book with ISBN " + request.isbn() + " already exists"
                    );
                });

        book.setIsbn(request.isbn());
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setGenre(request.genre());
        book.setYear(request.year());

        Book updatedBook = bookRepository.save(book);
        return toResponse(updatedBook);
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
    public void changeStatus(Long id, BookStatus newStatus) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new BookNotFoundException(
                                "Book with id " + id + " not found"
                        )
                );

        BookStatus currentStatus = book.getStatus();

        StatusTransitionStrategy strategy =
                statusTransitionStrategies.stream()
                        .filter(s -> s.supports(currentStatus, newStatus))
                        .findFirst()
                        .orElseThrow(() ->
                                new InvalidBookStatusTransitionException(
                                        "Cannot change book status from " + currentStatus + " to " + newStatus
                                )
                        );

        strategy.apply(book);
        bookRepository.save(book);
    }

    private BookResponse toResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getYear(),
                book.getStatus().name()
        );
    }
}