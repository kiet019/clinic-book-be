package com.c1se62.clinic_booking.service.DepartmentServices;

import com.c1se62.clinic_booking.dto.request.DepartmentRequestDTO;
import com.c1se62.clinic_booking.dto.request.DoctorReassignmentDTO;
import com.c1se62.clinic_booking.dto.response.DepartmentResponseDTO;

import java.util.List;

public interface DepartmentService {
    DepartmentResponseDTO createDepartment(DepartmentRequestDTO request);
    DepartmentResponseDTO updateDepartment(Integer id, DepartmentRequestDTO request);
    void deleteDepartment(Integer id);
    DepartmentResponseDTO getDepartmentById(Integer id);
    List<DepartmentResponseDTO> getAllDepartments();
    void reassignDoctors(DoctorReassignmentDTO request);
} 