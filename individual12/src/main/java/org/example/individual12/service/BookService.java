package org.example.individual12.service;

import org.example.individual12.dto.BookRequest;
import org.example.individual12.dto.BookResponse;

import java.util.List;

public interface BookService {

    BookResponse createBook(BookRequest request);

    BookResponse getBookById(Long id);

    List<BookResponse> getAllBooks();

    BookResponse updateBook(Long id, BookRequest request);

    void deleteBook(Long id);

    List<BookResponse> searchBooksByTitle(String title);

    List<BookResponse> searchBooksByGenre(String genre);
}