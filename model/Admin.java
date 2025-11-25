package model;

import java.util.Objects;
import java.util.UUID;

/**
 * مدل مدیر سیستم (Admin)
 * در نسخه کنسول ساده، فقط شناسه و اعتبارسنجی ذخیره می‌شود.
 */
public class Admin {
    private final String id;
    private String fullName;
    private String username;
    private String password;

    public Admin(String fullName, String username, String password) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Admin)) return false;
        Admin admin = (Admin) o;
        return Objects.equals(id, admin.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Admin{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}

