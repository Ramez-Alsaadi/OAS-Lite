/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package com.ramez.oas.oas.attendance;

import java.time.Duration;
import com.ramez.oas.oas.Employee;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Employee employee;

    private LocalDate attendanceDate;

    private LocalDateTime checkIn;

    private LocalDateTime checkOut;
    
     private String departureStatus;
        
     private Integer overtimeMinutes;


    public Attendance() {
    }

    public Long getId() {
        return id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public LocalDateTime getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDateTime checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDateTime getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDateTime checkOut) {
        this.checkOut = checkOut;
    }
    
    public String getArrivalStatus( ) {
        if (checkIn == null) {
            return "No Checked In";
        }
        LocalTime startTime = LocalTime.of(6,0);
        LocalTime actualTime = checkIn.toLocalTime();
        if(actualTime.isAfter(startTime)) {
            return "Late";
        }
        return "On Time";
    }
    
    public String getDepartureStatus( ) {
        if (checkOut == null) {
            return "No Checked Out";
        }
        LocalTime endTime = LocalTime.of(17,0);
        LocalTime actualTime = checkOut.toLocalTime();
        if(actualTime.isBefore(endTime)) {
            return "Early Departure";
        }
        return "Completed";
    }
    
    public Integer getOvertimeMinutes( ) {
        if (checkOut == null) {
            return 0;
        }
        LocalTime endTime = LocalTime.of(17,0);
        LocalTime actualTime = checkOut.toLocalTime();
        if(actualTime.isAfter(endTime)) {
            return (int) Duration.between(endTime, actualTime).toMinutes();
        }
        return 0;
    }
    
    
public void setDepartureStatus(String departureStatus) {
    this.departureStatus = departureStatus;
}

public void setOvertimeMinutes(Integer overtimeMinutes) {
    this.overtimeMinutes = overtimeMinutes;
}


            
}


