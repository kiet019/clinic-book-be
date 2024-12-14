package com.c1se62.clinic_booking.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionDTO {
    private Integer medicineId;
    private String dosage;
    private String name;
    private String description;
    private Integer stock;
}
