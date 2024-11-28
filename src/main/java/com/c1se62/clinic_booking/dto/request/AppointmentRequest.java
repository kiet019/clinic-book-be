package com.c1se62.clinic_booking.dto.request;

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
public class AppointmentRequest {
    private Integer doctorId;
    private LocalDate bookingDate;
    private LocalTime timeStart;
}
