package service;

import model.Book;
import repository.BookRepository;

import java.util.List;
import java.util.Optional;

public class BookService {

    private final BookRepository bookRepo;

    public BookService(BookRepository bookRepo) {
        this.bookRepo = bookRepo;
    }

    public void addBook(Book book) {
        bookRepo.save(book);
    }

    public Optional<Book> findById(String id) {
        return bookRepo.findById(id);
    }

    public List<Book> getAllBooks() {
        return bookRepo.findAll();
    }
}

