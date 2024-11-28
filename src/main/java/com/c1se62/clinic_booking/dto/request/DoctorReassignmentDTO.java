package com.c1se62.clinic_booking.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorReassignmentDTO {
    @NotEmpty(message = "Doctor IDs list cannot be empty")
    private List<Integer> doctorIds;
    
    @NotNull(message = "Target department ID is required")
    private Integer targetDepartmentId;
} 