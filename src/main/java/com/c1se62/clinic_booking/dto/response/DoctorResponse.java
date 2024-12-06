package com.c1se62.clinic_booking.dto.response;

import com.c1se62.clinic_booking.entity.Department;
import com.c1se62.clinic_booking.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorResponse {
    private Integer doctorId;
    private String doctorName;
    private String department;
    private String speciality;
    private String bio;
    private Double rating;
    private String email;
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
}
