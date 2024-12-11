package com.c1se62.clinic_booking.controller;

import com.c1se62.clinic_booking.dto.response.PrescriptionResponseDTO;
import com.c1se62.clinic_booking.entity.Prescription;
import com.c1se62.clinic_booking.service.PrescriptionServices.PrescriptionServices;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prescriptions")
@AllArgsConstructor
public class PrescriptionController {
    @Autowired
    PrescriptionServices prescriptionServices;

    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionResponseDTO> getPrescriptionByAppo(@PathVariable int id) {

            PrescriptionResponseDTO responseDTO = prescriptionServices.getPrescriptionByAppointmentId(id);
            if (responseDTO != null) {
                return ResponseEntity.ok(responseDTO); // 200 OK
            } else {
                return ResponseEntity.notFound().build(); // 404 Not Found
            }

    }

}
