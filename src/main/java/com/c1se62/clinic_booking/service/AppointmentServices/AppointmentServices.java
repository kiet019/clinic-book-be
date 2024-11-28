package com.c1se62.clinic_booking.service.AppointmentServices;

import com.c1se62.clinic_booking.dto.request.AppointmentRequest;
import com.c1se62.clinic_booking.dto.request.PrescriptionCreateDTO;
import com.c1se62.clinic_booking.dto.response.AppointmentDTO;
import com.c1se62.clinic_booking.dto.response.DoctorDashboardResponse;
import com.c1se62.clinic_booking.entity.Appointment;
import com.c1se62.clinic_booking.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AppointmentServices {
    String addAppointment(AppointmentRequest appointment,User user);
    boolean addPrescriptions(List<PrescriptionCreateDTO> prescriptions, Integer appointmentId);
    public List<AppointmentDTO> getAppointmentsByUserId(Integer userId) ;

    List<DoctorDashboardResponse> getAppointmentsByDoctorId(Integer doctorId);
}
