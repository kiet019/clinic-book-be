package com.c1se62.clinic_booking.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicineUpdatedDTO {
    private String name;
    
    private String description;
    
    @Min(value = 0, message = "Stock quantity must be greater than or equal to 0")
    private Integer stock;
    
    private String manufacturer;
    
    @Min(value = 0, message = "Unit price must be greater than or equal to 0")
    private Double unitPrice;
    
    private String expirationDate;
} 