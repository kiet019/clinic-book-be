package com.c1se62.clinic_booking.mapper;

import com.c1se62.clinic_booking.dto.request.AppointmentRequest;
import com.c1se62.clinic_booking.entity.Appointment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface AppointmentMapper {
    Appointment toAppointment(AppointmentRequest appointment);
}
