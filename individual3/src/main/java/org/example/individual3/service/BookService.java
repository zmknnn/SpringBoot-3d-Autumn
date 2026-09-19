package org.example.individual3.service;


import org.example.individual3.dto.BookRequest;
import org.example.individual3.dto.BookResponse;
import org.example.individual3.entity.BookStatus;

import java.util.List;

public interface BookService {

    BookResponse createBook(BookRequest request);

    BookResponse getBookById(Long id);

    List<BookResponse> getAllBooks();

    BookResponse updateBook(Long id, BookRequest request);

    void deleteBook(Long id);

    void changeStatus(Long id, BookStatus newStatus);
}
