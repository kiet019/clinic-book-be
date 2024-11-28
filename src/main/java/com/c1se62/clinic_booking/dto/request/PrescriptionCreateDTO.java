package com.c1se62.clinic_booking.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PrescriptionCreateDTO {
    private Integer medicineId;
    private String dosage;
}
