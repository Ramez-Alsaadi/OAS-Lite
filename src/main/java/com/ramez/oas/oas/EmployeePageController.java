/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.ramez.oas.oas;

import com.ramez.oas.oas.service.EmployeeService;
import com.ramez.oas.oas.Employee;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmployeePageController {

    private final EmployeeService employeeService;

    public EmployeePageController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    public String employeesPage(org.springframework.ui.Model model) {

        model.addAttribute("employees", employeeService.getAllEmployees());

        return "employees";
    }
    
    @GetMapping("/employees/new")
    public String newEmployeePage(){
        return "employee-form";
    }
}



