package com.c1se62.clinic_booking.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentRequestDTO {
    @NotNull(message = "Department name is required")
    @NotEmpty(message = "Department name is required")
    @Size(min = 3, message = "Department name must be at least 3 characters")
    private String name;
}
