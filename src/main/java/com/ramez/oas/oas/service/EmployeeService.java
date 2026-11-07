/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.ramez.oas.oas.service;

import com.ramez.oas.oas.Employee;
import com.ramez.oas.oas.repository.EmployeeRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public long getTotalEmployees() {
        return employeeRepository.count();
    }

    public Employee saveEmployee(Employee employee) {

        if (employee == null ||
            employee.getEmployeeCode() == null ||
            employee.getEmployeeCode().isBlank()) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Employee ID is required"
            );
        }

        String employeeCode = employee.getEmployeeCode().trim();

        if (employeeRepository.existsByEmployeeCode(employeeCode)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Employee ID already exists"
            );
        }

        employee.setEmployeeCode(employeeCode);

        return employeeRepository.save(employee);
    }
    
    
    public Employee updateEmployeeStatus(Long id, boolean active) {
    Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Employee not found"
            ));

    employee.setActive(active);

    return employeeRepository.save(employee);
}

}

