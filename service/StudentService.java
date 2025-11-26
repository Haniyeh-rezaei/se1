package service;

import model.Book;
import model.LoanRequest;
import model.Student;
import repository.BookRepository;
import repository.LoanRepository;
import repository.StudentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class StudentService {

    private final StudentRepository studentRepo;
    private final BookRepository bookRepo;
    private final LoanRepository loanRepo;

    public StudentService(StudentRepository studentRepo,
                          BookRepository bookRepo,
                          LoanRepository loanRepo) {

        this.studentRepo = studentRepo;
        this.bookRepo = bookRepo;
        this.loanRepo = loanRepo;
    }

    // ثبت نام دانشجو
    public boolean register(String fullName, String username, String password) {
        if (studentRepo.findByUsername(username).isPresent()) return false;

        Student s = new Student(fullName, username, password);
        studentRepo.save(s);
        return true;
    }

    // ورود دانشجو
    public Student login(String username, String password) {
        Optional<Student> optional = studentRepo.findByUsername(username);
        if (optional.isPresent() && optional.get().getPassword().equals(password)) {
            return optional.get();
        }
        return null;
    }

    // جستجوی کتاب
    public List<Book> searchBooks(String title, Integer year, String author) {
        return bookRepo.search(title, year, author);
    }

    // ثبت درخواست امانت
    public boolean requestLoan(Student student, String bookId, LocalDate start, LocalDate end) {

        if (!student.isActive()) return false; // دانشجو غیرفعال باشد → حق امانت ندارد

        Optional<Book> bookOpt = bookRepo.findById(bookId);
        if (bookOpt.isEmpty()) return false;

        LoanRequest request = new LoanRequest(student.getId(), bookId, start, end);
        loanRepo.saveLoanRequest(request);

        return true;
    }
}

