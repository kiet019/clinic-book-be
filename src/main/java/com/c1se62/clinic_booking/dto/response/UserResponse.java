package com.c1se62.clinic_booking.dto.response;

import jakarta.persistence.ElementCollection;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private int id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean isActive;
    private String phoneNumber;
    @ElementCollection
    private Set<String> roles;
}
