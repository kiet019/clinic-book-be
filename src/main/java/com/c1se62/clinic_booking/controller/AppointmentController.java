
package com.c1se62.clinic_booking.controller;
import com.c1se62.clinic_booking.dto.request.PrescriptionCreateDTO;
import com.c1se62.clinic_booking.dto.response.DoctorDashboardResponse;
import com.c1se62.clinic_booking.service.AppointmentServices.AppointmentServices;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@AllArgsConstructor
public class AppointmentController {
    AppointmentServices appointmentServices;

    @PostMapping("/{id}/prescription")
    public ResponseEntity<Boolean> addPrescription(@PathVariable Integer id,
                                                   @RequestBody List<PrescriptionCreateDTO> prescriptions) {
        return ResponseEntity.ok(appointmentServices.addPrescriptions(prescriptions, id));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<DoctorDashboardResponse>> getAppointmentsByDoctorId(@PathVariable Integer doctorId) {
        List<DoctorDashboardResponse> appointments = appointmentServices.getAppointmentsByDoctorId(doctorId);
        return ResponseEntity.ok(appointments);
    }
}