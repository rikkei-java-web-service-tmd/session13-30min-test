package com.example.library.service;

import com.example.library.entity.Book;
import com.example.library.exception.BookNotFoundException;
import com.example.library.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        log.debug("Get all books request");
        List<Book> books = bookRepository.findAll();
        log.info("Get all books successfully, total: {}", books.size());
        return books;
    }

    public Book getBookById(Long id) {
        log.debug("Get book by id request, id: {}", id);
        return bookRepository.findById(id)
                .map(book -> {
                    log.info("Get book by id successfully, id: {}", id);
                    return book;
                })
                .orElseThrow(() -> {
                    log.error("Book not found, id: {}", id);
                    return new BookNotFoundException(id);
                });
    }

    public Book createBook(Book book) {
        log.debug("Create book request: {}", book);
        Book savedBook = bookRepository.save(book);
        log.info("Create book successfully, id: {}", savedBook.getId());
        return savedBook;
    }

    public Book updateBook(Long id, Book book) {
        Book existing = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        existing.setTitle(book.getTitle());
        existing.setAuthor(book.getAuthor());
        existing.setCategory(book.getCategory());
        existing.setQuantity(book.getQuantity());
        return bookRepository.save(existing);
    }

    public Book patchBook(Long id, Book book) {
        Book existing = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        if (book.getTitle() != null) existing.setTitle(book.getTitle());
        if (book.getAuthor() != null) existing.setAuthor(book.getAuthor());
        if (book.getCategory() != null) existing.setCategory(book.getCategory());
        if (book.getQuantity() != null) existing.setQuantity(book.getQuantity());
        return bookRepository.save(existing);
    }

    public void deleteBook(Long id) {
        Book existing = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        bookRepository.delete(existing);
    }
}
