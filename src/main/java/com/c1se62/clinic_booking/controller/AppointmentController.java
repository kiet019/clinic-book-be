
package com.c1se62.clinic_booking.controller;
import com.c1se62.clinic_booking.dto.request.PrescriptionCreateDTO;
import com.c1se62.clinic_booking.dto.response.AppointmentDTO;
import com.c1se62.clinic_booking.dto.response.DoctorDashboardResponse;
import com.c1se62.clinic_booking.entity.Doctor;
import com.c1se62.clinic_booking.entity.User;
import com.c1se62.clinic_booking.repository.DoctorRepository;
import com.c1se62.clinic_booking.repository.UserRepository;
import com.c1se62.clinic_booking.service.AppointmentServices.AppointmentServices;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@AllArgsConstructor
public class AppointmentController {
    AppointmentServices appointmentServices;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DoctorRepository doctorRepository;
    @PostMapping("/{id}/prescription")
    public ResponseEntity<Boolean> addPrescription(@PathVariable Integer id,
                                                   @RequestBody List<PrescriptionCreateDTO> prescriptions) {
        return ResponseEntity.ok(appointmentServices.addPrescriptions(prescriptions, id));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByDoctorId(@PathVariable Integer doctorId) {
        List<AppointmentDTO> appointments = appointmentServices.getAppointmentsByDoctorId(doctorId);
        return ResponseEntity.ok(appointments);
    }
    @GetMapping("/doctor")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByDoctor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
        String userIdStr = jwt.getClaim("sub");
        User user = userRepository.findByUsername(userIdStr)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
        Doctor doctor= doctorRepository.findByUserId(user.getUserId());
        List<AppointmentDTO> appointments = appointmentServices.getAppointmentsByDoctorId(doctor.getDoctorId());
        return ResponseEntity.ok(appointments);
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        List<AppointmentDTO> appointments = appointmentServices.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }
}