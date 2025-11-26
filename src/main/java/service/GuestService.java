package service;

import model.Book;
import repository.BookRepository;
import repository.LoanRepository;
import repository.StudentRepository;

import java.util.List;

public class GuestService {

    private final StudentRepository studentRepo;
    private final BookRepository bookRepo;
    private final LoanRepository loanRepo;

    public GuestService(StudentRepository studentRepo,
                        BookRepository bookRepo,
                        LoanRepository loanRepo) {
        this.studentRepo = studentRepo;
        this.bookRepo = bookRepo;
        this.loanRepo = loanRepo;
    }

    public long countStudents() {
        return studentRepo.count();
    }

    public long countBooks() {
        return bookRepo.count();
    }

    public long countLoanRecords() {
        return loanRepo.countAllRecords();
    }

    public long countCurrentlyLoanedBooks() {
        return loanRepo.countLoanedBooks();
    }

    public void printBasicStats() {
        System.out.println("تعداد دانشجویان: " + countStudents());
        System.out.println("تعداد کتاب‌ها: " + countBooks());
        System.out.println("کل امانت‌ها: " + countLoanRecords());
        System.out.println("کتاب‌های در حال امانت: " + countCurrentlyLoanedBooks());
    }

    // جستجوی کتاب و چاپ مستقیم داخل متد
    public void searchByTitle(String title) {
        List<Book> books = bookRepo.searchByTitle(title == null ? "" : title);
        if (books.isEmpty()) {
            System.out.println("هیچ کتابی یافت نشد.");
        } else {
            books.forEach(System.out::println);
        }
    }

    // اگر نیاز داری تعداد کل دانشجویان را به صورت String برگردانی
    public String totalStudents() {
        return String.valueOf(studentRepo.count());
    }

    public long totalBooks() {
        return bookRepo.count();
    }

    public long totalLoans() {
        return loanRepo.countAllRecords();
    }

    public long activeLoans() {
        return loanRepo.countLoanedBooks();
    }
}
