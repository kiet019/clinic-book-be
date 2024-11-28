package com.c1se62.clinic_booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorUpdatedDTO {
    private String bio;
    private String speciality;
    private Integer departmentId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String country;
    private Date dateOfBirth;
    private String bloodgroup;
}
