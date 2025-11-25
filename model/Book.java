package model;

import java.util.Objects;
import java.util.UUID;

/**
 * مدل کتاب
 */
public class Book {
    private final String id;
    private String title;
    private String author;
    private int publishYear;

    private boolean available; // آیا در حال حاضر کتاب آزاد است؟

    public Book(String title, String author, int publishYear) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.author = author;
        this.publishYear = publishYear;
        this.available = true; // کتاب تازه ثبت شده → موجود است
    }

    public String getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public int getPublishYear() { return publishYear; }
    public void setPublishYear(int publishYear) { this.publishYear = publishYear; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publishYear=" + publishYear +
                ", available=" + available +
                '}';
    }
}

