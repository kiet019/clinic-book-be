package com.c1se62.clinic_booking.dto.response;

import com.c1se62.clinic_booking.entity.Doctor;
import com.c1se62.clinic_booking.entity.Prescription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionResponseDTO {
    private String username;
    private String email;
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String country;
    private Date dateOfBirth;
    private String doctorName;
    private String departmentName;
    private Doctor doctor;
    private LocalDate date;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private Set<PrescriptionDTO> prescriptionDTO;

}
