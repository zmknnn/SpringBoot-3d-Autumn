package org.example.individual5.repository;

import org.example.individual5.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    Book save(Book book);

    Optional<Book> findById(Long id);

    Optional<Book> findByIsbn(String isbn);

    List<Book> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);

    List<Book> findByTitle(String title);
}
