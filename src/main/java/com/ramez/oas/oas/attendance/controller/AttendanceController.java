/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.ramez.oas.oas.attendance.controller;
import com.ramez.oas.oas.Employee;
import com.ramez.oas.oas.attendance.Attendance;
import com.ramez.oas.oas.attendance.service.AttendanceService;
import com.ramez.oas.oas.repository.EmployeeRepository;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final EmployeeRepository employeeRepository;

    public AttendanceController(
            AttendanceService attendanceService,
            EmployeeRepository employeeRepository) {

        this.attendanceService = attendanceService;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping
    public List<Attendance> getAllAttendance() {
        return attendanceService.getAllAttendance();
    }

    @PostMapping("/check-in/{employeeId}")
    public Attendance checkIn(@PathVariable Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"Employee not found"));
        if(!employee.isActive()){
            throw new ResponseStatusException(
            HttpStatus.FORBIDDEN,"Employee account is inactive.Attendance registration denied.");
        }

        return attendanceService.checkIn(employee);
    }
    
    
    @PostMapping("/check-out/{attendanceId}")
    public Attendance checkOut(@PathVariable Long attendanceId) {

        Attendance attendance = attendanceService
                .getAllAttendance()
                .stream()
                .filter(a -> a.getId().equals(attendanceId))
                .findFirst()
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"Attendance not found"));

        return attendanceService.checkout(attendance);
    }


}


