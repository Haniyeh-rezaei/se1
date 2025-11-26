package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * مدل دانشجو
 */
public class Student {
    private final String id;        // شناسه یکتا (UUID)
    private String fullName;
    private String username;
    private String password;
    private boolean active;         // وضعیت فعال/غیرفعال (پیش‌فرض فعال)
    private final List<model.LoanRecord> loanHistory; // تاریخچه امانت‌ها (کلاس LoanRecord در ادامه اضافه می‌شود)

    public Student(String fullName, String username, String password) {
        this.id = UUID.randomUUID().toString();
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.active = true;
        this.loanHistory = new ArrayList<>();
    }

    // getter / setter
    public String getId() { return id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public List<model.LoanRecord> getLoanHistory() { return loanHistory; }

    // افزودن رکورد امانت به تاریخچه
    public void addLoanRecord(model.LoanRecord record) {
        if (record != null) loanHistory.add(record);
    }

    // مقایسه بر اساس id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", username='" + username + '\'' +
                ", active=" + active +
                ", loanHistoryCount=" + loanHistory.size() +
                '}';
    }
}

