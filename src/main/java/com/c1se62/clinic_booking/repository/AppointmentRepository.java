package com.c1se62.clinic_booking.repository;

import com.c1se62.clinic_booking.dto.response.AppointmentDTO;
import com.c1se62.clinic_booking.dto.response.DoctorDashboardResponse;
import com.c1se62.clinic_booking.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    Appointment save(Appointment newAppointment);
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
            "FROM Appointment a " +
            "WHERE a.user.userId = :userId " +
            "AND a.doctor.doctorId = :doctorId " +
            "AND a.status = :status")
    boolean existsByUserIdAndDoctorIdAndStatus(Integer userId, Integer doctorId, String status);
    @Modifying
    void deleteByUserUserId(Integer userId);
    @Query("SELECT a FROM Appointment a WHERE a.user.userId = :userId ORDER BY a.timeSlot.date DESC, a.timeSlot.timeStart DESC")
    List<Appointment> findByUserId(@Param("userId") Integer userId);
    @Query("SELECT  a " +
            "FROM Appointment a " +
            "WHERE a.doctor.doctorId = :doctorId")
    List<Appointment> findAppointmentsByDoctorId(@Param("doctorId") Integer doctorId);
}
