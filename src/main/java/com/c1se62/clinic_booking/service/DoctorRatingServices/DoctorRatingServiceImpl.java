package com.c1se62.clinic_booking.service.DoctorRatingServices;

import com.c1se62.clinic_booking.dto.DoctorRatingDTO;
import com.c1se62.clinic_booking.dto.request.RatingRequest;
import com.c1se62.clinic_booking.entity.Doctor;
import com.c1se62.clinic_booking.entity.DoctorRating;
import com.c1se62.clinic_booking.entity.User;
import com.c1se62.clinic_booking.exception.APIException;
import com.c1se62.clinic_booking.exception.ResourceNotFoundException;
import com.c1se62.clinic_booking.repository.AppointmentRepository;
import com.c1se62.clinic_booking.repository.DoctorRatingRepository;
import com.c1se62.clinic_booking.repository.DoctorRepository;
import com.c1se62.clinic_booking.repository.UserRepository;
import com.c1se62.clinic_booking.service.SecurityServices.SecurityService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class DoctorRatingServiceImpl implements DoctorRatingService{
    DoctorRatingRepository doctorRatingRepository;
    AppointmentRepository appointmentRepository;
    DoctorRepository doctorRepository;
    UserRepository userRepository;
    SecurityService securityService;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DoctorRatingDTO addRating(RatingRequest ratingRequest, User user) {
        // Validate rating value
        if (ratingRequest.getRating() < 1 || ratingRequest.getRating() > 5) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Rating must be between 1 and 5");
        }

        // Check doctor exists
        Doctor doctor = doctorRepository.findById(ratingRequest.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "Id", ratingRequest.getDoctorId()));

        // Verify completed appointment
        boolean hasAppointment = appointmentRepository.existsByUserIdAndDoctorIdAndStatus(
                user.getUserId(), doctor.getDoctorId(), "COMPLETED");
        if (!hasAppointment) {
            throw new APIException(HttpStatus.BAD_REQUEST,
                    "You must complete an appointment with this doctor before rating");
        }

        // Create and save rating
        DoctorRating doctorRating = new DoctorRating();
        doctorRating.setDoctor(doctor);
        doctorRating.setPatient(user);
        doctorRating.setRating(ratingRequest.getRating());
        doctorRating.setComment(ratingRequest.getComment());

        try {
            doctorRatingRepository.save(doctorRating);
            return mapToDoctorRatingDTO(doctorRating);
        } catch (Exception e) {
            throw new APIException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to save rating: " + e.getMessage());
        }
    }

    @Override
    public List<DoctorRatingDTO> getDoctorRatingsByDoctorId(Integer doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "Id", doctorId));
        List<DoctorRating> doctorRatings = doctorRatingRepository.findByDoctorDoctorId(doctor.getDoctorId());
        return doctorRatings.stream().map(this::mapToDoctorRatingDTO).toList();
    }

    @Override
    public List<DoctorRatingDTO> getDoctorRatingsForCurrentDoctor(Integer userId) {
        Doctor doctor = doctorRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "UserId", userId));
        List<DoctorRating> doctorRatings = doctorRatingRepository.findByDoctorDoctorId(doctor.getDoctorId());
        return doctorRatings.stream().map(this::mapToDoctorRatingDTO).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRating(Integer ratingId) {
        if (!securityService.hasRole("ROLE_ADMIN")) {
            throw new APIException(HttpStatus.FORBIDDEN, "You are not allowed to delete ratings.");
        }
        User user = securityService.getCurrentUser();
        DoctorRating doctorRating = doctorRatingRepository.findById(ratingId).orElseThrow(
                () -> new ResourceNotFoundException("DoctorRating", "Id", ratingId));
        if (doctorRating.getPatient().getUserId() != user.getUserId())
            throw new APIException(HttpStatus.FORBIDDEN, "You are not allowed to delete this rating.");
        doctorRatingRepository.delete(doctorRating);
    }
    private @NotNull DoctorRatingDTO mapToDoctorRatingDTO(DoctorRating doctorRating) {
        DoctorRatingDTO doctorRatingDTO = new DoctorRatingDTO();
        doctorRatingDTO.setDoctorId(doctorRating.getDoctor().getDoctorId());
        doctorRatingDTO.setPatientId(doctorRating.getPatient().getUserId());
        doctorRatingDTO.setRating(doctorRating.getRating());
        doctorRatingDTO.setComment(doctorRating.getComment());
        return doctorRatingDTO;
    }
}
