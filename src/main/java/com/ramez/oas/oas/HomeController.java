/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package com.ramez.oas.oas;

import com.ramez.oas.oas.service.EmployeeService;
import com.ramez.oas.oas.attendance.service.AttendanceService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final EmployeeService employeeService;
    private final AttendanceService attendanceService;

    public HomeController(
            EmployeeService employeeService,
            AttendanceService attendanceService) {

        this.employeeService = employeeService;
        this.attendanceService = attendanceService;
    }

    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute(
                "totalEmployees",
                employeeService.getTotalEmployees()
        );

        model.addAttribute(
                "employees",
                employeeService.getAllEmployees()
        );

        model.addAttribute(
                "attendanceRecords",
                attendanceService.getAllAttendance()
        );
        
        System.out.println("Dashboard attendance records: " + attendanceService.getAllAttendance().size());

        return "dashboard";
    }
}


