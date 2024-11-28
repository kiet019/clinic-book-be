package com.c1se62.clinic_booking.controller;

import com.c1se62.clinic_booking.dto.request.DepartmentRequestDTO;
import com.c1se62.clinic_booking.dto.request.DoctorReassignmentDTO;
import com.c1se62.clinic_booking.dto.response.DepartmentResponseDTO;
import com.c1se62.clinic_booking.service.DepartmentServices.DepartmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@AllArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<DepartmentResponseDTO> createDepartment(@Valid @RequestBody DepartmentRequestDTO request) {
        return new ResponseEntity<>(departmentService.createDepartment(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> updateDepartment(
            @PathVariable Integer id,
            @Valid @RequestBody DepartmentRequestDTO request) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Integer id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> getDepartmentById(@PathVariable Integer id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }

    @GetMapping
    public ResponseEntity<List<DepartmentResponseDTO>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @PutMapping("/reassign-doctors")
    public ResponseEntity<Void> reassignDoctors(@Valid @RequestBody DoctorReassignmentDTO request) {
        departmentService.reassignDoctors(request);
        return ResponseEntity.ok().build();
    }
} 