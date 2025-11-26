package repository;

import model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookRepository {

    private final List<Book> books = new ArrayList<>();

    public void save(Book book) {
        books.add(book);
    }

    public Optional<Book> findById(String id) {
        return books.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst();
    }

    public List<Book> findAll() {
        return new ArrayList<>(books);
    }

    // جستجو برای دانشجو (عنوان یا نویسنده یا سال)
    public List<Book> search(String title, Integer year, String author) {
        return books.stream()
                .filter(b ->
                        (title == null || b.getTitle().contains(title)) &&
                                (year == null || b.getPublishYear() == year) &&
                                (author == null || b.getAuthor().contains(author))
                )
                .collect(Collectors.toList());
    }

    // جستجوی مهمان بر اساس عنوان فقط
    public List<Book> searchByTitle(String title) {
        return books.stream()
                .filter(b -> b.getTitle().contains(title))
                .collect(Collectors.toList());
    }

    public long count() {
        return books.size();
    }
}

