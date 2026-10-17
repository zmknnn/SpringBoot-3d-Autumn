package org.example.individual7.book;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BookStore {

    private final Map<Long, Book> books = new HashMap<>();

    private long nextId = 1;

    public List<Book> findAll() {
        return new ArrayList<>(books.values());
    }

    public Book save(Book book) {
        book.setId(nextId);
        books.put(nextId, book);
        nextId++;
        return book;
    }
}