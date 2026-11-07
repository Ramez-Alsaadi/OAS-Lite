/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.ramez.oas.oas.attendance.service;

import com.ramez.oas.oas.Employee;
import com.ramez.oas.oas.attendance.Attendance;
import com.ramez.oas.oas.attendance.repository.AttendanceRepository;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceService(
            AttendanceRepository attendanceRepository) {

        this.attendanceRepository = attendanceRepository;
    }

    // تسجيل الحضور
    public Attendance checkIn(Employee employee) {

        LocalDate today = LocalDate.now();

        boolean alreadyCheckedIn =
                attendanceRepository.existsByEmployee_IdAndAttendanceDateAndCheckOutIsNull(
                        employee.getId(),
                        today
                );

        if (alreadyCheckedIn) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Employee already has an open attendance record"
            );
            
            
        }
        
       
        Attendance attendance = new Attendance();

        attendance.setEmployee(employee);
        attendance.setAttendanceDate(today);
        attendance.setCheckIn(LocalDateTime.now());

        return attendanceRepository.save(attendance);
    }

    // تسجيل الانصراف
    public Attendance checkout(Attendance attendance) {

        if (attendance.getCheckOut() != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Attendance already checked out."
            );
        }

        if (attendance.getCheckIn() == null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Employee must check in before checking out"
            );
        }

        if (attendance.getCheckOut() != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Employee already checked out"
            );
        }

        
LocalDateTime checkoutTime = LocalDateTime.now();

attendance.setCheckOut(checkoutTime);

LocalDateTime officialEnd = checkoutTime
        .toLocalDate()
        .atTime(17, 0);

if (checkoutTime.isBefore(officialEnd)) {

    attendance.setDepartureStatus("Early Departure");

    attendance.setOvertimeMinutes(0);

} else {

    attendance.setDepartureStatus("Completed");

    int overtime = (int) java.time.Duration.between(
            officialEnd,
            checkoutTime
    ).toMinutes();

    attendance.setOvertimeMinutes(overtime);

}



        return attendanceRepository.save(attendance);
    }

    // جلب جميع سجلات الحضور
    public List<Attendance> getAllAttendance() {

        return attendanceRepository
                .findAllByOrderByAttendanceDateDescCheckInDesc();
    }
}

