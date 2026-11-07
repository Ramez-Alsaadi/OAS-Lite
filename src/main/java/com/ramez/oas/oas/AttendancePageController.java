/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.ramez.oas.oas;

import com.ramez.oas.oas.repository.EmployeeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AttendancePageController {

    private final EmployeeRepository employeeRepository;

    public AttendancePageController(
            EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/attendance-management")
    public String attendanceManagement(Model model) {

        model.addAttribute(
                "employees",
                employeeRepository.findAll()
        );

        return "attendance-management";
    }
}

