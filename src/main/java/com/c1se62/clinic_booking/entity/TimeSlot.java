package com.c1se62.clinic_booking.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;


@Entity
@Table(name = "TimeSlots")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "time_slot_id")
    private Integer timeSlotId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @Column(name = "date", nullable = true)
    private LocalDate date;

    @Column(name = "time_start", nullable = false)
    private LocalTime timeStart;

    @Column(name = "time_end", nullable = false)
    private LocalTime timeEnd;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TimeSlotStatus status;
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    @OneToMany(mappedBy = "timeSlot", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private Set<Appointment> appointments;

    public enum TimeSlotStatus {
        AVAILABLE, BOOKED, CANCELLED
    }
}