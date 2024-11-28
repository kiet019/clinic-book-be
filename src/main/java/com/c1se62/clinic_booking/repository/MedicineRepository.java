package com.c1se62.clinic_booking.repository;

import com.c1se62.clinic_booking.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Integer> {
    @Query("SELECT m FROM Medicine m WHERE m.medicineId IN :ids")
    List<Medicine> findByIds(@Param("ids") List<Integer> ids);
    List<Medicine> findByStockLessThan(Integer threshold);
    List<Medicine> findByNameContainingIgnoreCase(String name);
}
