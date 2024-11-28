package com.c1se62.clinic_booking.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClinicInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clinic_id")
    private Integer clinicId;

    @Column(name = "clinic_name", nullable = false)
    private String clinicName;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "opening_hours")
    private String openingHours;

    @Column(name = "about_us")
    private String aboutUs;
}
