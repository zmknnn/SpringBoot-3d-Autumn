package org.example.individual5.service;

import org.example.individual5.dto.BookRequest;
import org.example.individual5.dto.BookResponse;
import org.example.individual5.entity.Book;
import org.example.individual5.entity.BookStatus;
import org.example.individual5.exception.BookNotFoundException;
import org.example.individual5.exception.DuplicateIsbnException;
import org.example.individual5.repository.BookRepository;
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
        when(bookRepository.save(any(Book.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        BookResponse result = bookService.updateBook(1L, request);

        assertThat(result.title()).isEqualTo("The Song of Achilles — Updated");
        assertThat(result.year()).isEqualTo(2020);
        assertThat(result.genre()).isEqualTo("Fantasy");
        assertThat(result.author()).isEqualTo("Madeline Miller");

        verify(bookRepository).save(any(Book.class));
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
    void shouldGetAllBooksSuccessfully() {
        Book firstBook = new Book(
                1L,
                "978-1234567890",
                "The Song of Achilles",
                "Madeline Miller",
                "Historical Fiction",
                2011,
                BookStatus.AVAILABLE
        );

        Book secondBook = new Book(
                2L,
                "978-0987654321",
                "Red, White & Royal Blue",
                "Casey McQuiston",
                "Romance",
                2019,
                BookStatus.AVAILABLE
        );

        when(bookRepository.findAll())
                .thenReturn(List.of(firstBook, secondBook));

        List<BookResponse> result = bookService.getAllBooks();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).title()).isEqualTo("The Song of Achilles");
        assertThat(result.get(1).title()).isEqualTo("Red, White & Royal Blue");

        verify(bookRepository).findAll();
    }

    @Test
    void shouldSearchBooksByTitleSuccessfully() {
        Book book = new Book(
                1L,
                "978-1234567890",
                "The Song of Achilles",
                "Madeline Miller",
                "Historical Fiction",
                2011,
                BookStatus.AVAILABLE
        );

        when(bookRepository.findByTitle("Song"))
                .thenReturn(List.of(book));

        List<BookResponse> result = bookService.searchBooksByTitle("Song");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).title()).isEqualTo("The Song of Achilles");
        assertThat(result.get(0).author()).isEqualTo("Madeline Miller");

        verify(bookRepository).findByTitle("Song");
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