package com.c1se62.clinic_booking.service.TimeSlotServices;

import com.c1se62.clinic_booking.dto.response.TimeslotResponse;
import com.c1se62.clinic_booking.entity.Doctor;
import com.c1se62.clinic_booking.entity.TimeSlot;
import com.c1se62.clinic_booking.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TimeSlotServicesImpl implements TimeSlotServices{
    @Autowired
    private TimeSlotRepository timeSlotRepository;
    @Override
    public List<TimeslotResponse> getAvailableTimeSlots(Integer doctorId) {
        // Lấy ngày hiện tại
        LocalDate today = LocalDate.now();

        // Xác định ngày bắt đầu (Thứ Hai) và ngày kết thúc (Chủ Nhật) của tuần hiện tại
        LocalDate startOfWeek = today.with(java.time.DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(java.time.DayOfWeek.SUNDAY);

        // Lấy danh sách TimeSlot từ Repository
        List<TimeSlot> timeSlots = timeSlotRepository.findByDoctorIdAndDateAndStatus(
                doctorId, startOfWeek, endOfWeek, TimeSlot.TimeSlotStatus.AVAILABLE);

        // Chuyển đổi sang DTO
        return timeSlots.stream()
                .map(timeSlot -> {
                    TimeslotResponse timeslotResponse = new TimeslotResponse();
                    timeslotResponse.setTimeSlotId(timeSlot.getTimeSlotId());
                    timeslotResponse.setTimeStart(timeSlot.getTimeStart());
                    timeslotResponse.setTimeEnd(timeSlot.getTimeEnd());
                    timeslotResponse.setDayOfWeek(timeSlot.getDayOfWeek());
                    return timeslotResponse;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<TimeSlot> generateTimeSlotsForWeek(Doctor doctor, Map<DayOfWeek, List<LocalTime[]>> timeSchedule) {
        List<TimeSlot> timeSlots = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY); // Xác định ngày bắt đầu của tuần

        for (DayOfWeek day : DayOfWeek.values()) {
            LocalDate date = startOfWeek.with(day); // Tính ngày cho từng thứ trong tuần
            List<LocalTime[]> dailyTimes = timeSchedule.get(day);            if (dailyTimes != null) {
                for (LocalTime[] times : dailyTimes) {
                    if (times.length == 2) {
                        LocalTime timeStart = times[0];
                        LocalTime timeEnd = times[1];

                        TimeSlot timeSlot = new TimeSlot();
                        timeSlot.setDoctor(doctor);
                        timeSlot.setDayOfWeek(day);
                        timeSlot.setTimeStart(timeStart);
                        timeSlot.setTimeEnd(timeEnd);
                        timeSlot.setDate(date);
                        timeSlot.setStatus(TimeSlot.TimeSlotStatus.AVAILABLE);

                        timeSlots.add(timeSlot);
                    }
                }
            }
        }

        timeSlotRepository.saveAll(timeSlots);
        return timeSlots;
    }

}
