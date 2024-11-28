package com.c1se62.clinic_booking.repository;

import com.c1se62.clinic_booking.entity.TimeSlot;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Integer> {
    @Query("SELECT ts FROM TimeSlot ts WHERE ts.doctor.doctorId = :doctorId " +
            "AND ts.date BETWEEN :startOfWeek AND :endOfWeek " +
            "AND ts.status = :status")
    List<TimeSlot> findByDoctorIdAndDateAndStatus(
            @Param("doctorId") Integer doctorId,
            @Param("startOfWeek") LocalDate startOfWeek,
            @Param("endOfWeek") LocalDate endOfWeek,
            @Param("status") TimeSlot.TimeSlotStatus status);
    @Query("SELECT ts FROM TimeSlot ts WHERE ts.doctor.doctorId = :doctorId " +
            "AND ts.timeStart <= :startDate AND ts.timeEnd >= :startDate AND ts.date <= :Date ")
    Optional<TimeSlot> findByDoctorIdAndDateAndTimeStart(@Param("doctorId") Integer doctorId,
                                                         @Param("startDate") LocalTime startDate,
                                                         @Param("Date") LocalDate Date
                                                         );

    @Transactional
    @Modifying
    @Query("DELETE FROM TimeSlot ts WHERE ts.doctor.doctorId = :doctorId AND ts.status = 'AVAILABLE'")
    void deleteAllByDoctorId(@Param("doctorId") Integer doctorId);

}