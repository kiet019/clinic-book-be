package com.c1se62.clinic_booking.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicineCreatedDTO {
    @NotBlank(message = "Medicine name is required")
    private String name;
    private String description;
    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity must be greater than or equal to 0")
    private Integer stock;
    private String manufacturer;
    @NotNull(message = "Unit price is required")
    @Min(value = 0, message = "Unit price must be greater than or equal to 0")
    private Double unitPrice;
    private String expirationDate;
} 