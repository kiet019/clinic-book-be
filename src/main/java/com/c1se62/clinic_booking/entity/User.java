package com.c1se62.clinic_booking.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String username;
    private String password;
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
    @ElementCollection
    private Set<String> role;
    private Boolean isActive;
}
