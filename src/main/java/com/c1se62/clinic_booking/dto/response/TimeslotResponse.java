package com.c1se62.clinic_booking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeslotResponse {
    private Integer timeSlotId;
    private LocalDate date;
    private LocalTime timeEnd;
    private LocalTime timeStart;
    private DayOfWeek dayOfWeek;
}
