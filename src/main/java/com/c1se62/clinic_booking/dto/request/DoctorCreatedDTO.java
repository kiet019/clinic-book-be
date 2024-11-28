package com.c1se62.clinic_booking.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorCreatedDTO {
    @NotNull(message = "Doctor bio is required")
    @NotEmpty(message = "Doctor bio is required")
    @Size(min = 10, message = "Doctor bio must be at least 10 characters")
    private String bio;
    @NotNull(message = "Doctor speciality is required")
    @NotEmpty(message = "Doctor speciality is required")
    @Size(min = 5, message = "Doctor speciality must be at least 5 characters")
    private String speciality;
    @NotNull(message = "Department id is required")
    private int departmentId;
    @NotNull(message = "Username is required")
    @NotEmpty(message = "Username is required")
    @Size(min = 5, message = "Username must be at least 5 characters")
    private String username;
    
    @NotNull(message = "Password is required") 
    @NotEmpty(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
    
    @NotNull(message = "First name is required")
    @NotEmpty(message = "First name is required")
    private String firstName;
    
    @NotNull(message = "Last name is required")
    @NotEmpty(message = "Last name is required")
    private String lastName;
    
    @NotNull(message = "Email is required")
    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @NotNull(message = "Phone number is required")
    @NotEmpty(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String country;
    private Date dateOfBirth;
    private String bloodgroup;
}
