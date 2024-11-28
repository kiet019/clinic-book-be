package com.c1se62.clinic_booking.repository;

import com.c1se62.clinic_booking.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    Department findByName(String depamentname);
}
