package com.c1se62.clinic_booking.service.DepartmentServices;

import com.c1se62.clinic_booking.dto.request.DepartmentRequestDTO;
import com.c1se62.clinic_booking.dto.request.DoctorReassignmentDTO;
import com.c1se62.clinic_booking.dto.response.DepartmentResponseDTO;
import com.c1se62.clinic_booking.entity.Department;
import com.c1se62.clinic_booking.entity.Doctor;
import com.c1se62.clinic_booking.exception.APIException;
import com.c1se62.clinic_booking.exception.ResourceNotFoundException;
import com.c1se62.clinic_booking.repository.DepartmentRepository;
import com.c1se62.clinic_booking.repository.DoctorRepository;
import com.c1se62.clinic_booking.service.SecurityServices.SecurityService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    DepartmentRepository departmentRepository;
    SecurityService securityService;
    DoctorRepository doctorRepository;

    @Override
    @Transactional
    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO request) {
        securityService.validateAdminAccess();
        if (departmentRepository.findByName(request.getName()) != null) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Department with this name already exists");
        }
        Department department = new Department();
        department.setName(request.getName());
        Department savedDepartment = departmentRepository.save(department);
        return mapToDepartmentResponse(savedDepartment);
    }
    @Override
    @Transactional
    public DepartmentResponseDTO updateDepartment(Integer id, DepartmentRequestDTO request) {
        securityService.validateAdminAccess();
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "Id", id));
        Department existingDepartment = departmentRepository.findByName(request.getName());
        if (existingDepartment != null && !existingDepartment.getDepartmentId().equals(id)) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Department with this name already exists");
        }
        department.setName(request.getName());
        Department updatedDepartment = departmentRepository.save(department);
        return mapToDepartmentResponse(updatedDepartment);
    }

    @Override
    @Transactional
    public void deleteDepartment(Integer id) {
        securityService.validateAdminAccess();
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "Id", id));
        if (department.getDoctors() != null && !department.getDoctors().isEmpty()) {
            throw new APIException(HttpStatus.BAD_REQUEST,
                    "Cannot delete department that has doctors. Please reassign or remove doctors first.");
        }
        departmentRepository.delete(department);
    }

    @Override
    public DepartmentResponseDTO getDepartmentById(Integer id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "Id", id));
        return mapToDepartmentResponse(department);
    }

    @Override
    public List<DepartmentResponseDTO> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream()
                .map(this::mapToDepartmentResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void reassignDoctors(DoctorReassignmentDTO request) {
        securityService.validateAdminAccess();
        Department targetDepartment = departmentRepository.findById(request.getTargetDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "Id", request.getTargetDepartmentId()));
        List<Doctor> doctors = doctorRepository.findAllById(request.getDoctorIds());
        if (doctors.size() != request.getDoctorIds().size()) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Some doctor IDs are invalid");
        }
        doctors.forEach(doctor -> {
            doctor.setDepartment(targetDepartment);
        });
        doctorRepository.saveAll(doctors);
    }

    private DepartmentResponseDTO mapToDepartmentResponse(Department department) {
        return DepartmentResponseDTO.builder()
                .departmentId(department.getDepartmentId())
                .name(department.getName())
                .doctorCount(department.getDoctors() != null ? department.getDoctors().size() : 0)
                .build();
    }
} 