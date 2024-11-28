package com.c1se62.clinic_booking.service.TimeSlotServices;

import com.c1se62.clinic_booking.dto.response.TimeslotResponse;
import com.c1se62.clinic_booking.entity.Doctor;
import com.c1se62.clinic_booking.entity.TimeSlot;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
public interface TimeSlotServices {
    public List<TimeslotResponse> getAvailableTimeSlots(Integer doctorId);
    public List<TimeSlot> generateTimeSlotsForWeek(Doctor doctor, Map<DayOfWeek, List<LocalTime[]>> timeSchedule);
}
