/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ramez.oas.oas.attendance.repository;

import com.ramez.oas.oas.attendance.Attendance;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    boolean existsByEmployeeIdAndAttendanceDate(
            Long employeeId,
            LocalDate attendanceDate
    );
    List<Attendance>
            findAllByOrderByAttendanceDateDescCheckInDesc();
            
            boolean existsByEmployee_IdAndAttendanceDateAndCheckOutIsNull(
            Long employeeId, LocalDate attendanceDate);
}

