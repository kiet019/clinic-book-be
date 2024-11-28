package com.c1se62.clinic_booking.service.DoctorRatingServices;

import com.c1se62.clinic_booking.dto.DoctorRatingDTO;
import com.c1se62.clinic_booking.dto.request.RatingRequest;
import com.c1se62.clinic_booking.entity.DoctorRating;
import com.c1se62.clinic_booking.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DoctorRatingService {
    DoctorRatingDTO addRating(RatingRequest ratingRequest, User user);
    List<DoctorRatingDTO> getDoctorRatingsByDoctorId(Integer doctorId);
    List<DoctorRatingDTO> getDoctorRatingsForCurrentDoctor(Integer userId);
    void deleteRating(Integer ratingId);
}
