package org.example.individual4.repository;

import org.example.individual4.entity.Book;
import org.example.individual4.entity.BookStatus;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcBookRepository implements BookRepository {

    private final JdbcClient jdbcClient;

    public JdbcBookRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Book save(Book book) {
        String statusName = (book.status() != null) ? book.status().name() : BookStatus.AVAILABLE.name();

        if (book.id() == null) {
            jdbcClient
                    .sql("""
                            INSERT INTO books (isbn, title, author, genre, publication_year, status)
                            VALUES (:isbn, :title, :author, :genre, :year, :status)
                         """)
                    .param("isbn", book.isbn())
                    .param("title", book.title())
                    .param("author", book.author())
                    .param("genre", book.genre())
                    .param("year", book.year())
                    .param("status", statusName)
                    .update();

            return findByIsbn(book.isbn()).orElseThrow();
        }

        jdbcClient
                .sql("""
                    UPDATE books
                    SET isbn = :isbn,
                        title = :title,
                        author = :author,
                        genre = :genre,
                        publication_year = :year,
                        status = :status
                    WHERE id = :id
                    """)
                .param("id", book.id())
                .param("isbn", book.isbn())
                .param("title", book.title())
                .param("author", book.author())
                .param("genre", book.genre())
                .param("year", book.year())
                .param("status", statusName)
                .update();

        return findById(book.id()).orElseThrow();
    }

    @Override
    public Optional<Book> findById(Long id) {
        return jdbcClient
                .sql("""
                SELECT id, isbn, title, author, genre,
                       publication_year AS "year", status
                FROM books
                WHERE id = :id
                """)
                .param("id", id)
                .query(Book.class)
                .optional();
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return jdbcClient
                .sql("""
                SELECT id, isbn, title, author, genre,
                       publication_year AS "year", status
                FROM books
                WHERE isbn = :isbn
                """)
                .param("isbn", isbn)
                .query(Book.class)
                .optional();
    }

    @Override
    public List<Book> findAll() {
        return jdbcClient
                .sql("""
                SELECT id, isbn, title, author, genre,
                       publication_year AS "year", status
                FROM books
                """)
                .query(Book.class)
                .list();
    }

    @Override
    public void deleteById(Long id) {
        jdbcClient
                .sql("DELETE FROM books WHERE id = :id")
                .param("id", id)
                .update();
    }

    @Override
    public boolean existsById(Long id) {
        return jdbcClient
                .sql("SELECT COUNT(*) FROM books WHERE id = :id")
                .param("id", id)
                .query(Long.class)
                .single() > 0;
    }

    @Override
    public List<Book> findByTitle(String title) {
        return jdbcClient
                .sql("""
                SELECT id, isbn, title, author, genre,
                       publication_year AS "year", status
                FROM books
                WHERE LOWER(title) LIKE LOWER(:title)
                """)
                .param("title", "%" + title + "%")
                .query(Book.class)
                .list();
    }
}