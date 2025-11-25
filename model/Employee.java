package model;

import java.util.Objects;
import java.util.UUID;

/**
 * مدل کارمند کتابخانه
 */
public class Employee {
    private final String id;
    private String fullName;
    private String username;
    private String password;

    // آمار ساده برای گزارش مدیر (آپدیت توسط سرویس‌ها)
    private int registeredBooksCount = 0;
    private int loansGivenCount = 0;     // تعداد امانت‌هایی که کارمند تایید کرده (یا تحویل داده)
    private int loansReceivedCount = 0;  // تعداد کتاب‌هایی که کارمند دریافت (تحویل) کرده

    public Employee(String fullName, String username, String password) {
        this.id = UUID.randomUUID().toString();
        this.fullName = fullName;
        this.username = username;
        this.password = password;
    }

    public String getId() { return id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getRegisteredBooksCount() { return registeredBooksCount; }
    public void incrementRegisteredBooksCount() { this.registeredBooksCount++; }

    public int getLoansGivenCount() { return loansGivenCount; }
    public void incrementLoansGivenCount() { this.loansGivenCount++; }

    public int getLoansReceivedCount() { return loansReceivedCount; }
    public void incrementLoansReceivedCount() { this.loansReceivedCount++; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", username='" + username + '\'' +
                ", registeredBooks=" + registeredBooksCount +
                ", loansGiven=" + loansGivenCount +
                ", loansReceived=" + loansReceivedCount +
                '}';
    }
}

