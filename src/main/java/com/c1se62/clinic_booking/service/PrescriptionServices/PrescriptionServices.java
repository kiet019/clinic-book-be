package com.c1se62.clinic_booking.service.PrescriptionServices;

import com.c1se62.clinic_booking.dto.response.PrescriptionResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface PrescriptionServices {
    PrescriptionResponseDTO getPrescriptionByAppointmentId(int appointmentId);
}
