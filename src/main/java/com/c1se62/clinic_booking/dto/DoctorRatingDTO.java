package com.c1se62.clinic_booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorRatingDTO {
    private Integer rating;
    private String comment;
    private Integer doctorId;
    private Integer patientId;
}
