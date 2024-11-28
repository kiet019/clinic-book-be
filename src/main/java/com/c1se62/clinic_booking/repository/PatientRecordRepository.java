package com.c1se62.clinic_booking.repository;

import com.c1se62.clinic_booking.entity.PatientRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRecordRepository extends JpaRepository<PatientRecord, Integer> {
    @Modifying
    void deleteByPatientUserId(Integer userId);
    @Query("select p from PatientRecord p where p.patient.userId = :userId and p.doctor.doctorId = :doctorId")
    List<PatientRecord> findByPatientUserIdAndDoctorId(Integer userId, Integer doctorId);
    @Query("select p from PatientRecord p where p.patient.userId = :userId")
    List<PatientRecord> findByPatientUserId(Integer userId);
    @Query("select p from PatientRecord p where p.doctor.doctorId = :doctorId")
    List<PatientRecord> findByDoctorId(Integer doctorId);
}
