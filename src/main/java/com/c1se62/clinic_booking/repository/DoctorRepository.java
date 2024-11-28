package com.c1se62.clinic_booking.repository;

import com.c1se62.clinic_booking.entity.Department;
import com.c1se62.clinic_booking.entity.Doctor;

import com.c1se62.clinic_booking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    @Query("SELECT d FROM Doctor d JOIN FETCH d.department JOIN FETCH d.user")
    List<Doctor> getAllDoctor();
    @Query("SELECT d FROM Doctor d JOIN FETCH d.department dept JOIN FETCH d.user " +
            "WHERE dept.departmentId = :departmentId")
    List<Doctor> getAllDoctorByDepartment(@Param("departmentId") Integer departmentId);
    Optional<Doctor> findByUserUserId(Integer userId);
    @Query("SELECT d FROM Doctor d JOIN FETCH d.user " +
            "WHERE d.user.userId = :userId")
    Doctor findByUserId(int userId);
}