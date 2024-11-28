package com.c1se62.clinic_booking.dto.response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorRatingResponse {
    private Integer ratingId;
    private Integer rating;
    private String comment;
    private String createdAt;
}
