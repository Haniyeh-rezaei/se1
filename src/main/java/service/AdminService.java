package service;

import model.Employee;
import model.LoanRecord;
import model.Student;

import repository.EmployeeRepository;
import repository.LoanRepository;
import repository.StudentRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AdminService {

    private final EmployeeRepository employeeRepo;
    private final StudentRepository studentRepo;
    private final LoanRepository loanRepo;

    public AdminService(EmployeeRepository employeeRepo,
                        StudentRepository studentRepo,
                        LoanRepository loanRepo) {
        this.employeeRepo = employeeRepo;
        this.studentRepo = studentRepo;
        this.loanRepo = loanRepo;
    }

    // تعریف کارمند
    public boolean registerEmployee(String fullName, String username, String password) {
        if (employeeRepo.findByUsername(username).isPresent()) return false;

        Employee emp = new Employee(fullName, username, password);
        employeeRepo.save(emp);
        return true;
    }

    // عملکرد کارمندان
    public void printEmployeePerformance() {
        System.out.println("---- گزارش عملکرد کارمندان ----");
        employeeRepo.findAll().forEach(emp -> {
            System.out.println(emp.getFullName() +
                    " | ثبت کتاب: " + emp.getRegisteredBooksCount() +
                    " | امانت داده: " + emp.getLoansGivenCount() +
                    " | تحویل گرفته: " + emp.getLoansReceivedCount());
        });
    }

    // آمار کلی امانت‌ها
    public void printLoanStats() {
        List<LoanRecord> records = loanRepo.findAllRecords();

        long requestCount = loanRepo.countRequests();
        long totalLoans = records.size();

        double avgDays = records.stream()
                .filter(LoanRecord::isReturned)
                .mapToLong(LoanRecord::getLoanDurationDays)
                .average()
                .orElse(0);

        System.out.println("---- آمار امانت‌ها ----");
        System.out.println("تعداد درخواست‌های ثبت‌شده: " + requestCount);
        System.out.println("تعداد کل امانت‌ها: " + totalLoans);
        System.out.println("میانگین مدت امانت (روز): " + avgDays);
    }

    // آمار دانشجویان + 10 دانشجوی با بیشترین تأخیر
    public void printStudentStats() {
        System.out.println("---- آمار دانشجویان ----");

        List<Student> students = studentRepo.findAll();

        for (Student s : students) {
            List<LoanRecord> recs = loanRepo.findByStudent(s.getId());
            long total = recs.size();
            long notReturned = recs.stream().filter(r -> !r.isReturned()).count();
            long delayed = recs.stream()
                    .filter(r -> r.isReturned() && r.getLoanDurationDays() > 7)
                    .count();

            System.out.println(s.getFullName() +
                    " | کل امانت‌ها: " + total +
                    " | تحویل نداده: " + notReturned +
                    " | تأخیر‌دار: " + delayed);
        }

        // 10 دانشجوی دارای بیشترین تأخیر
        System.out.println("\n---- 10 دانشجوی دارای بیشترین تأخیر ----");

        students.stream()
                .sorted((s1, s2) -> {
                    long d1 = loanRepo.findByStudent(s1.getId()).stream()
                            .filter(r -> r.isReturned() && r.getLoanDurationDays() > 7).count();
                    long d2 = loanRepo.findByStudent(s2.getId()).stream()
                            .filter(r -> r.isReturned() && r.getLoanDurationDays() > 7).count();
                    return Long.compare(d2, d1);
                })
                .limit(10)
                .forEach(s -> {
                    long delayed = loanRepo.findByStudent(s.getId()).stream()
                            .filter(r -> r.isReturned() && r.getLoanDurationDays() > 7)
                            .count();
                    System.out.println(s.getFullName() + " | تأخیر: " + delayed);
                });
    }
}

