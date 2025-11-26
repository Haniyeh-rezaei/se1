import model.Employee;
import model.LoanRequest;
import model.Student;
import repository.*;
import service.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        // ------------------ ساخت ریپازیتوری‌ها --------------------
        StudentRepository studentRepo = new StudentRepository();
        EmployeeRepository employeeRepo = new EmployeeRepository();
        BookRepository bookRepo = new BookRepository();
        LoanRepository loanRepo = new LoanRepository();

        // ------------------ ساخت سرویس‌ها -------------------------
        StudentService studentService = new StudentService(studentRepo, bookRepo, loanRepo);
        GuestService guestService = new GuestService(studentRepo, bookRepo, loanRepo);
        EmployeeService employeeService = new EmployeeService(employeeRepo, bookRepo, loanRepo, studentRepo);
        AdminService adminService = new AdminService(employeeRepo, studentRepo, loanRepo);

        // ------------------ ایجاد مدیر پیش‌فرض --------------------
        employeeRepo.save(new Employee("مدیر سیستم", "admin", "admin"));

        // ------------------ اجرای منوی اصلی ------------------------
        mainMenu(studentService, guestService, employeeService, adminService);
    }

    // ============================ MAIN MENU ============================
    private static void mainMenu(StudentService studentService,
                                 GuestService guestService,
                                 EmployeeService employeeService,
                                 AdminService adminService) {

        while (true) {
            System.out.println("\n===== سیستم مدیریت کتابخانه =====");
            System.out.println("1) ورود دانشجو");
            System.out.println("2) ثبت‌نام دانشجو");
            System.out.println("3) ورود کارمند");
            System.out.println("4) ورود مدیر");
            System.out.println("5) ورود به عنوان مهمان");
            System.out.println("6) خروج");

            System.out.print("گزینه را وارد کنید: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> studentLogin(studentService);
                case 2 -> studentRegister(studentService);
                case 3 -> employeeLogin(employeeService);
                case 4 -> adminLogin(adminService);
                case 5 -> guestMenu(guestService);
                case 6 -> {
                    System.out.println("خروج از سیستم...");
                    return;
                }
                default -> System.out.println("گزینه نامعتبر!");
            }
        }
    }

    // =========================== STUDENT REGISTER ============================
    private static void studentRegister(StudentService service) {
        System.out.println("\n--- ثبت‌نام دانشجو ---");
        System.out.print("نام و نام خانوادگی: ");
        String name = scanner.nextLine();

        System.out.print("نام کاربری: ");
        String username = scanner.nextLine();

        System.out.print("رمز عبور: ");
        String password = scanner.nextLine();

        if (service.register(name, username, password)) {
            System.out.println("ثبت‌نام با موفقیت انجام شد.");
        } else {
            System.out.println("نام کاربری تکراری است!");
        }
    }

    // =========================== STUDENT LOGIN ============================
    private static void studentLogin(StudentService service) {
        System.out.print("\nنام کاربری: ");
        String username = scanner.nextLine();

        System.out.print("رمز عبور: ");
        String password = scanner.nextLine();

        Student st = service.login(username, password);

        if (st == null) {
            System.out.println("اطلاعات ورود اشتباه است یا حساب غیر فعال است.");
            return;
        }

        studentMenu(service, st);
    }

    // =========================== STUDENT MENU ============================
    private static void studentMenu(StudentService service, Student st) {
        while (true) {
            System.out.println("\n--- منوی دانشجو ---");
            System.out.println("1) جستجوی کتاب");
            System.out.println("2) ثبت درخواست امانت");
            System.out.println("3) خروج");

            System.out.print("گزینه: ");
            int ch = Integer.parseInt(scanner.nextLine());

            switch (ch) {
                case 1 -> {
                    System.out.print("عنوان (خالی = هیچ): ");
                    String title = emptyToNull(scanner.nextLine());

                    System.out.print("نویسنده (خالی = هیچ): ");
                    String author = emptyToNull(scanner.nextLine());

                    System.out.print("سال نشر (خالی = هیچ): ");
                    String yearStr = scanner.nextLine();
                    Integer year = yearStr.isBlank() ? null : Integer.parseInt(yearStr);

                    service.searchBooks(title, year, author).forEach(System.out::println);
                }
                case 2 -> {
                    System.out.print("شناسه کتاب: ");
                    String bookId = scanner.nextLine();

                    System.out.print("تاریخ شروع (YYYY-MM-DD): ");
                    LocalDate start = LocalDate.parse(scanner.nextLine());

                    System.out.print("تاریخ پایان (YYYY-MM-DD): ");
                    LocalDate end = LocalDate.parse(scanner.nextLine());

                    boolean ok = service.requestLoan(st, bookId, start, end);
                    System.out.println(ok ? "درخواست ثبت شد." : "خطا در ثبت درخواست.");
                }
                case 3 -> { return; }
                default -> System.out.println("نامعتبر!");
            }
        }
    }

    // =========================== GUEST MENU ============================
    private static void guestMenu(GuestService service) {
        System.out.println("\n--- منوی مهمان ---");
        System.out.println("1) مشاهده تعداد دانشجویان");
        System.out.println("2) جستجو بر اساس نام کتاب");
        System.out.println("3) آمار ساده");

        System.out.print("گزینه: ");
        int ch = Integer.parseInt(scanner.nextLine());

        switch (ch) {
            case 1 ->
                    System.out.println("تعداد دانشجویان: " + service.countStudents());

            case 2 -> {
                System.out.print("نام کتاب: ");
                String title = scanner.nextLine();
                service.searchByTitle(title); // فقط صدا زده شود، خودش چاپ می‌کند
            }

            case 3 -> service.printBasicStats();

            default -> System.out.println("نامعتبر!");
        }
    }

    // =========================== EMPLOYEE LOGIN ============================
    private static void employeeLogin(EmployeeService service) {
        System.out.print("\nنام کاربری: ");
        String uname = scanner.nextLine();

        System.out.print("رمز عبور: ");
        String pass = scanner.nextLine();

        Employee emp = service.login(uname, pass);

        if (emp == null) {
            System.out.println("ورود نامعتبر!");
            return;
        }

        employeeMenu(service, emp);
    }

    // =========================== EMPLOYEE MENU ============================
    private static void employeeMenu(EmployeeService service, Employee emp) {
        while (true) {

            System.out.println("\n--- منوی کارمند ---");
            System.out.println("1) ثبت کتاب");
            System.out.println("2) جستجو/ویرایش کتاب");
            System.out.println("3) تایید درخواست‌ها");
            System.out.println("4) ثبت بازگشت کتاب");
            System.out.println("5) گزارش امانت دانشجو");
            System.out.println("6) خروج");

            System.out.print("گزینه: ");
            int c = Integer.parseInt(scanner.nextLine());

            switch (c) {
                case 1 -> {
                    System.out.print("عنوان: ");
                    String t = scanner.nextLine();
                    System.out.print("نویسنده: ");
                    String a = scanner.nextLine();
                    System.out.print("سال نشر: ");
                    int y = Integer.parseInt(scanner.nextLine());
                    service.registerBook(emp, t, a, y);
                }
                case 2 -> {
                    System.out.print("عنوان کتاب: ");
                    String t = scanner.nextLine();
                    service.searchBooks(t).forEach(System.out::println);

                    System.out.print("شناسه کتاب برای ویرایش: ");
                    String id = scanner.nextLine();

                    System.out.print("عنوان جدید (خالی = بدون تغییر): ");
                    String nt = emptyToNull(scanner.nextLine());

                    System.out.print("نویسنده جدید: ");
                    String na = emptyToNull(scanner.nextLine());

                    System.out.print("سال جدید: ");
                    String ny = scanner.nextLine();
                    Integer nyVal = ny.isBlank() ? null : Integer.parseInt(ny);

                    service.editBook(id, nt, na, nyVal);
                }
                case 3 -> {
                    List<LoanRequest> reqs = service.getRequestsForConfirmation();
                    reqs.forEach(System.out::println);

                    System.out.print("شناسه درخواست برای تایید: ");
                    String rid = scanner.nextLine();

                    reqs.stream()
                            .filter(r -> r.getId().equals(rid))
                            .findFirst()
                            .ifPresent(r -> {
                                System.out.println("1) تایید  2) رد");
                                int x = Integer.parseInt(scanner.nextLine());
                                if (x == 1) service.approveLoan(emp, r);
                                else service.rejectLoan(emp, r);
                            });
                }
                case 4 -> {
                    System.out.print("شناسه رکورد امانت: ");
                    String id = scanner.nextLine();
                    service.returnBook(emp, id);
                }
                case 5 -> {
                    System.out.print("شناسه دانشجو: ");
                    String id = scanner.nextLine();
                    service.printStudentLoanHistory(id);
                }
                case 6 -> { return; }
                default -> System.out.println("نامعتبر!");
            }
        }
    }

    // =========================== ADMIN LOGIN ============================
    private static void adminLogin(AdminService service) {
        System.out.print("\nنام کاربری مدیر: ");
        String u = scanner.nextLine();

        System.out.print("رمز عبور: ");
        String p = scanner.nextLine();

        if (!(u.equals("admin") && p.equals("admin"))) {
            System.out.println("ورود مدیر نامعتبر!");
            return;
        }

        adminMenu(service);
    }

    // =========================== ADMIN MENU ============================
    private static void adminMenu(AdminService admin) {
        while (true) {
            System.out.println("\n--- منوی مدیر ---");
            System.out.println("1) ثبت کارمند");
            System.out.println("2) گزارش عملکرد کارمندان");
            System.out.println("3) آمار امانت‌ها");
            System.out.println("4) آمار دانشجویان");
            System.out.println("5) خروج");

            System.out.print("گزینه: ");
            int c = Integer.parseInt(scanner.nextLine());

            switch (c) {
                case 1 -> {
                    System.out.print("نام کامل کارمند: ");
                    String name = scanner.nextLine();
                    System.out.print("نام کاربری: ");
                    String u = scanner.nextLine();
                    System.out.print("رمز عبور: ");
                    String p = scanner.nextLine();
                    admin.registerEmployee(name, u, p);
                }
                case 2 -> admin.printEmployeePerformance();
                case 3 -> admin.printLoanStats();
                case 4 -> admin.printStudentStats();
                case 5 -> { return; }
                default -> System.out.println("نامعتبر!");
            }
        }
    }

    // ======================= HELPER ============================
    private static String emptyToNull(String s) {
        return s.isBlank() ? null : s;
    }
}

