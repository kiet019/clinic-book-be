package com.c1se62.clinic_booking.controller;

import com.c1se62.clinic_booking.dto.DoctorRatingDTO;
import com.c1se62.clinic_booking.emuns.ERole;
import com.c1se62.clinic_booking.entity.User;
import com.c1se62.clinic_booking.service.DoctorRatingServices.DoctorRatingService;
import com.c1se62.clinic_booking.service.SecurityServices.SecurityService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/doctor-ratings")
@AllArgsConstructor
public class DoctorRatingController {
    DoctorRatingService doctorRatingService;
    SecurityService securityService;

    @GetMapping("/doctor/{id}")
    public ResponseEntity<List<DoctorRatingDTO>> getDoctorRatingsByDoctorId(@PathVariable Integer id) {
        List<DoctorRatingDTO> doctorRatings = doctorRatingService.getDoctorRatingsByDoctorId(id);
        return ResponseEntity.ok(doctorRatings);
    }

    @GetMapping("/")
    public ResponseEntity<List<DoctorRatingDTO>> getDoctorRatingsForCurrentDoctor() {
        User currentUser = securityService.getCurrentUser();
        if (!securityService.hasRole(ERole.DOCTOR.name())){
            return ResponseEntity.badRequest().build();
        }
        List<DoctorRatingDTO> doctorRatings = doctorRatingService.getDoctorRatingsForCurrentDoctor(currentUser.getUserId());
        return ResponseEntity.ok(doctorRatings);
    }
}
