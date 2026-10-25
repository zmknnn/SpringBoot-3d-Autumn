package org.example.individual8.service;

import org.example.individual8.dto.BookRequest;
import org.example.individual8.dto.BookResponse;

import java.util.List;

public interface BookService {

    BookResponse createBook(BookRequest request);

    BookResponse getBookById(Long id);

    List<BookResponse> getAllBooks();

    BookResponse updateBook(Long id, BookRequest request);

    void deleteBook(Long id);

    List<BookResponse> searchBooksByTitle(String title);
}