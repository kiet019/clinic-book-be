package com.c1se62.clinic_booking.controller;

import com.c1se62.clinic_booking.dto.request.PatientRecordCreatedDTO;
import com.c1se62.clinic_booking.dto.request.PrescriptionRequestDTO;
import com.c1se62.clinic_booking.dto.response.PatientRecordResponseDTO;
import com.c1se62.clinic_booking.service.PrescriptionServices.PrescriptionServices;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prescriptions")
@AllArgsConstructor
public class PrescriptionController {
    @Autowired
    PrescriptionServices prescriptionServices;
    @PostMapping("/create")
    public ResponseEntity<String> createPrescription(@RequestBody PrescriptionRequestDTO prescriptionRequestDTO){
        String response = prescriptionServices.createPrescription(prescriptionRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
