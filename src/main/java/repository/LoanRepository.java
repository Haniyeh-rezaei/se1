package repository;

import model.LoanRecord;
import model.LoanRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LoanRepository {

    private final List<LoanRequest> loanRequests = new ArrayList<>();
    private final List<LoanRecord> loanRecords = new ArrayList<>();

    // --- مدیریت درخواست‌ها ---

    public void saveLoanRequest(LoanRequest request) {
        loanRequests.add(request);
    }

    public List<LoanRequest> getPendingRequestsForConfirmation() {
        LocalDate today = LocalDate.now();

        return loanRequests.stream()
                .filter(r -> r.getStatus() == LoanRequest.Status.PENDING)
                .filter(r -> !r.getStartDate().isAfter(today)) // امروز یا قبل
                .collect(Collectors.toList());
    }

    public Optional<LoanRequest> findRequestById(String id) {
        return loanRequests.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst();
    }

    public List<LoanRequest> findAllRequests() {
        return new ArrayList<>(loanRequests);
    }

    public long countRequests() {
        return loanRequests.size();
    }

    // --- مدیریت رکوردهای امانت ---

    public void saveLoanRecord(LoanRecord record) {
        loanRecords.add(record);
    }

    public Optional<LoanRecord> findRecordById(String id) {
        return loanRecords.stream()
                .filter(l -> l.getId().equals(id))
                .findFirst();
    }

    public List<LoanRecord> findByStudent(String studentId) {
        return loanRecords.stream()
                .filter(l -> l.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }

    public List<LoanRecord> findAllRecords() {
        return new ArrayList<>(loanRecords);
    }

    public long countLoanedBooks() {
        return loanRecords.stream().filter(l -> !l.isReturned()).count();
    }

    public long countAllRecords() {
        return loanRecords.size();
    }
}

