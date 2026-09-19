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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private List<StatusTransitionStrategy> statusTransitionStrategies;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void shouldCreateBookSuccessfully() {
        BookRequest request = new BookRequest(
                "978-1234567890",
                "The Song of Achilles",
                2011,
                "Historical Fiction",
                "Madeline Miller"
        );

        when(bookRepository.findByIsbn(request.isbn()))
                .thenReturn(java.util.Optional.empty());

        Book savedBook = new Book(
                1L,
                request.isbn(),
                request.title(),
                request.author(),
                request.genre(),
                request.year(),
                BookStatus.AVAILABLE
        );

        when(bookRepository.save(any(Book.class)))
                .thenReturn(savedBook);

        BookResponse result = bookService.createBook(request);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.isbn()).isEqualTo(request.isbn());
        assertThat(result.title()).isEqualTo("The Song of Achilles");
        assertThat(result.author()).isEqualTo("Madeline Miller");
        assertThat(result.status()).isEqualTo("AVAILABLE");
    }

    @Test
    void shouldThrowExceptionWhenIsbnAlreadyExists() {
        BookRequest request = new BookRequest(
                "978-0987654321",
                "Red, White & Royal Blue",
                2019,
                "Romance",
                "Casey McQuiston"
        );

        Book existingBook = new Book(
                2L,
                request.isbn(),
                request.title(),
                request.author(),
                request.genre(),
                request.year(),
                BookStatus.AVAILABLE
        );

        when(bookRepository.findByIsbn(request.isbn()))
                .thenReturn(java.util.Optional.of(existingBook));

        assertThatThrownBy(() -> bookService.createBook(request))
                .isInstanceOf(DuplicateIsbnException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void shouldGetBookByIdSuccessfully() {
        Book book = new Book(
                1L,
                "978-1234567890",
                "The Song of Achilles",
                "Madeline Miller",
                "Historical Fiction",
                2011,
                BookStatus.AVAILABLE
        );

        when(bookRepository.findById(1L))
                .thenReturn(java.util.Optional.of(book));

        BookResponse result = bookService.getBookById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.title()).isEqualTo("The Song of Achilles");
        assertThat(result.author()).isEqualTo("Madeline Miller");
        assertThat(result.status()).isEqualTo("AVAILABLE");
    }

    @Test
    void shouldUpdateBookSuccessfully() {
        Book book = new Book(
                1L,
                "978-1234567890",
                "The Song of Achilles",
                "Madeline Miller",
                "Historical Fiction",
                2011,
                BookStatus.AVAILABLE
        );

        BookRequest request = new BookRequest(
                "978-1234567890",
                "The Song of Achilles — Updated",
                2020,
                "Fantasy",
                "Madeline Miller"
        );

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.findByIsbn(request.isbn())).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);

        BookResponse result = bookService.updateBook(1L, request);

        assertThat(result.title()).isEqualTo("The Song of Achilles — Updated");
        assertThat(result.year()).isEqualTo(2020);
        assertThat(result.genre()).isEqualTo("Fantasy");
        assertThat(result.author()).isEqualTo("Madeline Miller");

        verify(bookRepository).save(book);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingToExistingIsbn() {
        Book book = new Book(
                1L,
                "978-1234567890",
                "The Song of Achilles",
                "Madeline Miller",
                "Historical Fiction",
                2011,
                BookStatus.AVAILABLE
        );

        Book anotherBook = new Book(
                2L,
                "978-0987654321",
                "Red, White & Royal Blue",
                "Casey McQuiston",
                "Romance",
                2019,
                BookStatus.AVAILABLE
        );

        BookRequest request = new BookRequest(
                "978-0987654321",
                "The Song of Achilles",
                2011,
                "Historical Fiction",
                "Madeline Miller"
        );

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.findByIsbn(request.isbn()))
                .thenReturn(Optional.of(anotherBook));

        assertThatThrownBy(() -> bookService.updateBook(1L, request))
                .isInstanceOf(DuplicateIsbnException.class);

        verify(bookRepository, never()).save(book);
    }

    @Test
    void shouldThrowExceptionWhenBookNotFound() {
        when(bookRepository.findById(1L))
                .thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> bookService.getBookById(1L))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void shouldChangeBookStatusSuccessfully() {
        Book book = new Book(
                1L,
                "978-1234567890",
                "The Song of Achilles",
                "Madeline Miller",
                "Historical Fiction",
                2011,
                BookStatus.AVAILABLE
        );

        StatusTransitionStrategy strategy = mock(StatusTransitionStrategy.class);

        when(bookRepository.findById(1L))
                .thenReturn(java.util.Optional.of(book));

        when(strategy.supports(BookStatus.AVAILABLE, BookStatus.RESERVED))
                .thenReturn(true);

        when(statusTransitionStrategies.stream())
                .thenReturn(java.util.stream.Stream.of(strategy));

        bookService.changeStatus(1L, BookStatus.RESERVED);

        verify(strategy).apply(book);
        verify(bookRepository).save(book);
    }

    @Test
    void shouldThrowExceptionForInvalidStatusTransition() {
        Book book = new Book(
                2L,
                "978-0987654321",
                "Red, White & Royal Blue",
                "Casey McQuiston",
                "Romance",
                2019,
                BookStatus.RESERVED
        );

        StatusTransitionStrategy strategy = mock(StatusTransitionStrategy.class);

        when(bookRepository.findById(2L))
                .thenReturn(java.util.Optional.of(book));

        when(strategy.supports(BookStatus.RESERVED, BookStatus.BORROWED))
                .thenReturn(false);

        when(statusTransitionStrategies.stream())
                .thenReturn(java.util.stream.Stream.of(strategy));

        assertThatThrownBy(() ->
                bookService.changeStatus(2L, BookStatus.BORROWED))
                .isInstanceOf(InvalidBookStatusTransitionException.class)
                .hasMessageContaining("RESERVED")
                .hasMessageContaining("BORROWED");
    }

    @Test
    void shouldDeleteBookSuccessfully() {
        when(bookRepository.existsById(1L))
                .thenReturn(true);

        bookService.deleteBook(1L);

        verify(bookRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingBook() {
        when(bookRepository.existsById(999L))
                .thenReturn(false);

        assertThatThrownBy(() -> bookService.deleteBook(999L))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("999");

        verify(bookRepository, never()).deleteById(999L);
    }
}