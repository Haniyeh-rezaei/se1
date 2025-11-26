package repository;

import model.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeRepository {

    private final List<Employee> employees = new ArrayList<>();

    public void save(Employee employee) {
        employees.add(employee);
    }

    public Optional<Employee> findByUsername(String username) {
        return employees.stream()
                .filter(e -> e.getUsername().equals(username))
                .findFirst();
    }

    public Optional<Employee> findById(String id) {
        return employees.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();
    }

    public List<Employee> findAll() {
        return new ArrayList<>(employees);
    }

    public long count() {
        return employees.size();
    }
}

