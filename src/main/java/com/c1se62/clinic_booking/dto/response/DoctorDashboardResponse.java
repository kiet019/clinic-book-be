package com.c1se62.clinic_booking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDashboardResponse {
    private Integer doctorId;
    private Integer appointmentId;
    private String firstName;
    private String lastName;
    private LocalDate date;
    private LocalTime timeStart;
    private String appointmentType;
    private String status;
}
