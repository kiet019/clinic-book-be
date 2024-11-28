package com.c1se62.clinic_booking.service.PatientRecordServices;

import com.c1se62.clinic_booking.dto.request.PatientRecordCreatedDTO;
import com.c1se62.clinic_booking.dto.response.PatientRecordResponseDTO;

import java.util.List;

public interface PatientRecordService {
    List<PatientRecordResponseDTO> getPatientRecordsByDoctorIdAndPatientId(Integer doctorId, Integer patientId);

    List<PatientRecordResponseDTO> getPatientRecordsByPatientId(Integer patientId);

    List<PatientRecordResponseDTO> getPatientRecordsByDoctorId(Integer doctorId);

    PatientRecordResponseDTO createPatientRecord(PatientRecordCreatedDTO patientRecordCreatedDTO);

    PatientRecordResponseDTO updatePatientRecord(Integer recordId, PatientRecordCreatedDTO patientRecordCreatedDTO);

    void deletePatientRecordById(Integer recordId);
}
