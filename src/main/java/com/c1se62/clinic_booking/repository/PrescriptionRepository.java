package com.c1se62.clinic_booking.repository;

import com.c1se62.clinic_booking.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {
    List<Prescription> findByMedicineMedicineId(Integer medicineId);
}
