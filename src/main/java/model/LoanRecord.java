package model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * رکورد امانت کامل
 * وقتی کارمند درخواست را تایید کرد، رکورد ایجاد می‌شود.
 */
public class LoanRecord {

    private final String id;
    private final String studentId;
    private final String bookId;

    private final LocalDate startDate; // تاریخ شروع واقعی
    private LocalDate returnDate;      // تاریخ بازگشت واقعی

    public LoanRecord(String studentId, String bookId, LocalDate startDate) {
        this.id = UUID.randomUUID().toString();
        this.studentId = studentId;
        this.bookId = bookId;
        this.startDate = startDate;
        this.returnDate = null; // هنوز برگردانده نشده
    }

    public String getId() { return id; }

    public String getStudentId() { return studentId; }

    public String getBookId() { return bookId; }

    public LocalDate getStartDate() { return startDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public boolean isReturned() { return returnDate != null; }

    public long getLoanDurationDays() {
        if (returnDate == null) return -1;
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, returnDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoanRecord)) return false;
        LoanRecord that = (LoanRecord) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "LoanRecord{" +
                "id='" + id + '\'' +
                ", studentId='" + studentId + '\'' +
                ", bookId='" + bookId + '\'' +
                ", startDate=" + startDate +
                ", returnDate=" + returnDate +
                '}';
    }
}

