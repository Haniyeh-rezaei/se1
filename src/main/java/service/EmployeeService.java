package service;

import model.Book;
import model.Employee;
import model.LoanRecord;
import model.LoanRequest;
import model.Student;

import repository.BookRepository;
import repository.EmployeeRepository;
import repository.LoanRepository;
import repository.StudentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class EmployeeService {

    private final EmployeeRepository employeeRepo;
    private final BookRepository bookRepo;
    private final LoanRepository loanRepo;
    private final StudentRepository studentRepo;

    public EmployeeService(EmployeeRepository employeeRepo,
                           BookRepository bookRepo,
                           LoanRepository loanRepo,
                           StudentRepository studentRepo) {

        this.employeeRepo = employeeRepo;
        this.bookRepo = bookRepo;
        this.loanRepo = loanRepo;
        this.studentRepo = studentRepo;
    }

    // ورود کارمند
    public Employee login(String username, String password) {
        Optional<Employee> opt = employeeRepo.findByUsername(username);
        if (opt.isPresent() && opt.get().getPassword().equals(password))
            return opt.get();
        return null;
    }

    // تغییر رمز عبور
    public void changePassword(Employee employee, String newPass) {
        employee.setPassword(newPass);
    }

    // ثبت کتاب جدید
    public void registerBook(Employee employee, String title, String author, int year) {
        Book book = new Book(title, author, year);
        bookRepo.save(book);

        employee.incrementRegisteredBooksCount();
    }

    // جستجو و ویرایش کتاب
    public List<Book> searchBooks(String title) {
        return bookRepo.search(title, null, null);
    }

    public boolean editBook(String bookId, String newTitle, String newAuthor, Integer newYear) {
        Optional<Book> opt = bookRepo.findById(bookId);
        if (opt.isEmpty()) return false;

        Book b = opt.get();
        if (newTitle != null) b.setTitle(newTitle);
        if (newAuthor != null) b.setAuthor(newAuthor);
        if (newYear != null) b.setPublishYear(newYear);

        return true;
    }

    // تایید درخواست‌ها
    public List<LoanRequest> getRequestsForConfirmation() {
        return loanRepo.getPendingRequestsForConfirmation();
    }

    public boolean approveLoan(Employee employee, LoanRequest req) {
        Optional<Book> bookOpt = bookRepo.findById(req.getBookId());
        if (bookOpt.isEmpty()) return false;

        Book book = bookOpt.get();

        if (!book.isAvailable()) return false;

        req.setStatus(LoanRequest.Status.APPROVED);
        book.setAvailable(false);

        LoanRecord record = new LoanRecord(req.getStudentId(), req.getBookId(), req.getStartDate());
        loanRepo.saveLoanRecord(record);

        employee.incrementLoansGivenCount();
        return true;
    }

    public void rejectLoan(Employee employee, LoanRequest req) {
        req.setStatus(LoanRequest.Status.REJECTED);
    }

    // ثبت بازگشت کتاب
    public boolean returnBook(Employee employee, String recordId) {
        Optional<LoanRecord> recOpt = loanRepo.findRecordById(recordId);
        if (recOpt.isEmpty()) return false;

        LoanRecord rec = recOpt.get();
        if (rec.isReturned()) return false;

        rec.setReturnDate(LocalDate.now());

        bookRepo.findById(rec.getBookId()).ifPresent(b -> b.setAvailable(true));

        employee.incrementLoansReceivedCount();
        return true;
    }

    // فعال و غیرفعال کردن دانشجو
    public boolean setStudentActive(String studentId, boolean status) {
        Optional<Student> stOpt = studentRepo.findById(studentId);
        if (stOpt.isEmpty()) return false;

        stOpt.get().setActive(status);
        return true;
    }

    // گزارش تاریخچه امانت یک دانشجو
    public void printStudentLoanHistory(String studentId) {
        studentRepo.findById(studentId).ifPresentOrElse(student -> {
            List<LoanRecord> records = loanRepo.findByStudent(studentId);

            System.out.println("---- تاریخچه امانت دانشجو ----");
            System.out.println("نام: " + student.getFullName());
            System.out.println("تعداد کل امانت‌ها: " + records.size());

            long notReturned = records.stream().filter(r -> !r.isReturned()).count();
            long delayed = records.stream()
                    .filter(r -> r.isReturned() && r.getLoanDurationDays() > 7)
                    .count();

            System.out.println("کتاب‌های تحویل نداده: " + notReturned);
            System.out.println("کتاب‌های تحویل با تأخیر: " + delayed);

            System.out.println("لیست امانت‌ها:");
            records.forEach(System.out::println);

        }, () -> System.out.println("دانشجو یافت نشد."));
    }
}

