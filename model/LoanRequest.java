package model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * درخواست امانت کتاب توسط دانشجو
 * این درخواست بعداً توسط کارمند تأیید یا رد می‌شود
 */
public class LoanRequest {

    public enum Status {
        PENDING,     // در انتظار تأیید
        APPROVED,    // تأیید شده
        REJECTED     // رد شده
    }

    private final String id;
    private final String studentId;
    private final String bookId;
    private final LocalDate startDate;
    private final LocalDate endDate;

    private Status status;

    public LoanRequest(String studentId, String bookId, LocalDate startDate, LocalDate endDate) {
        this.id = UUID.randomUUID().toString();
        this.studentId = studentId;
        this.bookId = bookId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = Status.PENDING;
    }

    public String getId() { return id; }

    public String getStudentId() { return studentId; }

    public String getBookId() { return bookId; }

    public LocalDate getStartDate() { return startDate; }

    public LocalDate getEndDate() { return endDate; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoanRequest)) return false;
        LoanRequest that = (LoanRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "LoanRequest{" +
                "id='" + id + '\'' +
                ", studentId='" + studentId + '\'' +
                ", bookId='" + bookId + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +
                '}';
    }
}

