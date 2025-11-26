package service;

import model.Book;
import model.LoanRecord;
import model.LoanRequest;
import repository.BookRepository;
import repository.LoanRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class LoanService {

    private final LoanRepository loanRepo;
    private final BookRepository bookRepo;

    public LoanService(LoanRepository loanRepo, BookRepository bookRepo) {
        this.loanRepo = loanRepo;
        this.bookRepo = bookRepo;
    }

    // درخواست‌های در انتظار تأیید که تاریخشان امروز یا قبل است
    public List<LoanRequest> getRequestsForConfirmation() {
        return loanRepo.getPendingRequestsForConfirmation();
    }

    // تأیید درخواست
    public boolean approveRequest(LoanRequest req) {
        Optional<Book> bookOpt = bookRepo.findById(req.getBookId());
        if (bookOpt.isEmpty()) return false;
        Book book = bookOpt.get();

        if (!book.isAvailable()) return false;

        // تایید
        req.setStatus(LoanRequest.Status.APPROVED);

        // کتاب موجود نیست
        book.setAvailable(false);

        // ایجاد رکورد امانت
        LoanRecord record = new LoanRecord(req.getStudentId(), req.getBookId(), req.getStartDate());
        loanRepo.saveLoanRecord(record);

        return true;
    }

    // رد کردن درخواست
    public void rejectRequest(LoanRequest req) {
        req.setStatus(LoanRequest.Status.REJECTED);
    }

    // ثبت بازگشت کتاب
    public boolean returnBook(String recordId) {
        Optional<LoanRecord> recOpt = loanRepo.findRecordById(recordId);
        if (recOpt.isEmpty()) return false;

        LoanRecord record = recOpt.get();

        if (record.isReturned()) return false;

        record.setReturnDate(LocalDate.now());

        bookRepo.findById(record.getBookId()).ifPresent(b -> b.setAvailable(true));

        return true;
    }

    // گرفتن همه رکوردها
    public List<LoanRecord> getAllRecords() {
        return loanRepo.findAllRecords();
    }

    public List<LoanRecord> getStudentRecords(String studentId) {
        return loanRepo.findByStudent(studentId);
    }
}

