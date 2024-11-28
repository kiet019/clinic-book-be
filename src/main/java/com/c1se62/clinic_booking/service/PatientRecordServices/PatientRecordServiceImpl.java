package com.c1se62.clinic_booking.service.PatientRecordServices;

import com.c1se62.clinic_booking.dto.request.PatientRecordCreatedDTO;
import com.c1se62.clinic_booking.dto.response.PatientRecordResponseDTO;
import com.c1se62.clinic_booking.entity.Doctor;
import com.c1se62.clinic_booking.entity.PatientRecord;
import com.c1se62.clinic_booking.entity.User;
import com.c1se62.clinic_booking.repository.DoctorRepository;
import com.c1se62.clinic_booking.repository.PatientRecordRepository;
import com.c1se62.clinic_booking.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PatientRecordServiceImpl implements PatientRecordService{
    PatientRecordRepository patientRecordRepository;
    UserRepository userRepository;
    DoctorRepository doctorRepository;
    @Override
    public List<PatientRecordResponseDTO> getPatientRecordsByDoctorIdAndPatientId(Integer doctorId, Integer patientId) {
        List<PatientRecord> records = patientRecordRepository.findByPatientUserIdAndDoctorId(patientId, doctorId);
        return records.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PatientRecordResponseDTO> getPatientRecordsByPatientId(Integer patientId) {
        List<PatientRecord> records = patientRecordRepository.findByPatientUserId(patientId);
        return records.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    @Override
    public List<PatientRecordResponseDTO> getPatientRecordsByDoctorId(Integer doctorId) {
        List<PatientRecord> records = patientRecordRepository.findByDoctorId(doctorId);
        return records.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PatientRecordResponseDTO createPatientRecord(PatientRecordCreatedDTO patientRecordCreatedDTO) {
        PatientRecord response = new PatientRecord();
        User patient = userRepository.findById(patientRecordCreatedDTO.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        Doctor doctor = doctorRepository.findById(patientRecordCreatedDTO.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        response.setDoctor(doctor);
        response.setPatient(patient);
        response.setDiagnosis(patientRecordCreatedDTO.getDiagnosis());
        response.setTreatment(patientRecordCreatedDTO.getTreatment());
        response.setNotes(patientRecordCreatedDTO.getNotes());

        patientRecordRepository.save(response);
        return mapToDTO(response);
    }

    @Override
    public PatientRecordResponseDTO updatePatientRecord(Integer recordId, PatientRecordCreatedDTO patientRecordCreatedDTO) {
        PatientRecord response = patientRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Record not found"));
        User patient = userRepository.findById(patientRecordCreatedDTO.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        Doctor doctor = doctorRepository.findById(patientRecordCreatedDTO.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        response.setPatient(patient);
        response.setDoctor(doctor);
        response.setDiagnosis(patientRecordCreatedDTO.getDiagnosis());
        response.setTreatment(patientRecordCreatedDTO.getTreatment());
        response.setNotes(patientRecordCreatedDTO.getNotes());

        patientRecordRepository.save(response);
        return mapToDTO(response);
    }
    @Override
    public void deletePatientRecordById(Integer id) {
        PatientRecord patientRecord = patientRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found"));
        patientRecordRepository.delete(patientRecord);
    }

    private PatientRecordResponseDTO mapToDTO(PatientRecord patientRecord) {
        PatientRecordResponseDTO response = new PatientRecordResponseDTO();
        response.setRecordId(patientRecord.getRecordId());
        response.setPatientId(patientRecord.getPatient().getUserId());
        response.setDoctorId(patientRecord.getDoctor().getDoctorId());
        response.setRecordDate(patientRecord.getRecordDate());
        response.setDiagnosis(patientRecord.getDiagnosis());
        response.setTreatment(patientRecord.getTreatment());
        response.setNotes(patientRecord.getNotes());
        return response;
    }
}
