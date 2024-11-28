package com.c1se62.clinic_booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientRecordCreatedDTO {
    private Integer recordId;
    private Integer patientId;
    private Integer doctorId;
    private String recordDate;
    private String diagnosis;
    private String treatment;
    private String notes;
}
