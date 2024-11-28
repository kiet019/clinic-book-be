package com.c1se62.clinic_booking.controller;

import com.c1se62.clinic_booking.dto.request.PatientRecordCreatedDTO;
import com.c1se62.clinic_booking.dto.response.PatientRecordResponseDTO;
import com.c1se62.clinic_booking.service.PatientRecordServices.PatientRecordService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient-record")
@AllArgsConstructor
public class PatientRecordController {
    PatientRecordService patientRecordService;

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PatientRecordResponseDTO>> getPatientRecordsByPatientId(@PathVariable Integer patientId){
        List<PatientRecordResponseDTO> records = patientRecordService.getPatientRecordsByPatientId(patientId);
        return ResponseEntity.ok(records);
    }
    
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<PatientRecordResponseDTO>> getPatientRecordsByDoctorId(@PathVariable Integer doctorId){
        List<PatientRecordResponseDTO> records = patientRecordService.getPatientRecordsByDoctorId(doctorId);
        return ResponseEntity.ok(records);
    }

    @PostMapping("/create")
    public ResponseEntity<PatientRecordResponseDTO> createPatientRecord(@Valid @RequestBody PatientRecordCreatedDTO patientRecordDTO){
        PatientRecordResponseDTO response = patientRecordService.createPatientRecord(patientRecordDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update/{recordId}")
    public ResponseEntity<PatientRecordResponseDTO> updatePatientRecord(@PathVariable Integer recordId, @Valid @RequestBody PatientRecordCreatedDTO patientRecordDTO){
        PatientRecordResponseDTO response = patientRecordService.updatePatientRecord(recordId, patientRecordDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{recordId}")
    public ResponseEntity<Void> deletePatientRecordById(@PathVariable Integer recordId){
        patientRecordService.deletePatientRecordById(recordId);
        return ResponseEntity.noContent().build();
    }
}
