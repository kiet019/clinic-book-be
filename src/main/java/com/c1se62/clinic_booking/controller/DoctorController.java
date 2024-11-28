package com.c1se62.clinic_booking.controller;

import com.c1se62.clinic_booking.dto.request.DoctorCreatedDTO;
import com.c1se62.clinic_booking.dto.request.DoctorRequest;
import com.c1se62.clinic_booking.dto.request.DoctorUpdatedDTO;
import com.c1se62.clinic_booking.dto.request.UserRequest;
import com.c1se62.clinic_booking.dto.response.DoctorRatingResponse;
import com.c1se62.clinic_booking.dto.response.DoctorResponse;
import com.c1se62.clinic_booking.dto.response.TimeslotResponse;
import com.c1se62.clinic_booking.entity.Doctor;
import com.c1se62.clinic_booking.entity.TimeSlot;
import com.c1se62.clinic_booking.entity.User;
import com.c1se62.clinic_booking.repository.DoctorRepository;
import com.c1se62.clinic_booking.repository.TimeSlotRepository;
import com.c1se62.clinic_booking.repository.UserRepository;
import com.c1se62.clinic_booking.service.DoctorServices.DoctorServices;
import com.c1se62.clinic_booking.service.TimeSlotServices.TimeSlotServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private TimeSlotServices timeSlotServices;

    @Autowired
    private DoctorServices doctorServices;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @PostMapping("/generateTimeSlots")
    public ResponseEntity<?> generateTimeSlotsForWeek(
            @RequestBody Map<String, Object> request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Doctor doctor;
            if (!(authentication instanceof AnonymousAuthenticationToken)) {
                Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
                String userIdStr = jwt.getClaim("sub");
                User user = userRepository.findByUsername(userIdStr).orElse(null);
                doctor = doctorRepository.findByUserId(user.getUserId());
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn cần đăng nhập để thực hiện thao tác này");
            }
            if (doctor == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bác sĩ không tồn tại");
            }

            // Lấy thời gian khám cho từng ngày trong tuần từ request
            Map<String, Object> times = (Map<String, Object>) request.get("timeSchedule");
            Map<DayOfWeek, List<LocalTime[]>> timeSchedule = new HashMap<>();

            // Lấy dữ liệu từ request và chuyển thành map
            for (Map.Entry<String, Object> entry : times.entrySet()) {
                String day = entry.getKey();
                List<List<String>> timeSlots = (List<List<String>>) entry.getValue();
                List<LocalTime[]> dailyTimes = new ArrayList<>();

                for (List<String> time : timeSlots) {
                    LocalTime timeStart = LocalTime.parse(time.get(0));
                    LocalTime timeEnd = LocalTime.parse(time.get(1));
                    dailyTimes.add(new LocalTime[]{timeStart, timeEnd});
                }

                timeSchedule.put(DayOfWeek.valueOf(day.toUpperCase()), dailyTimes);
            }
            timeSlotRepository.deleteAllByDoctorId(doctor.getDoctorId());
            // Gọi phương thức tạo khung giờ khám cho bác sĩ
            List<TimeSlot> timeSlots = timeSlotServices.generateTimeSlotsForWeek(doctor, timeSchedule);

            // Trả về danh sách khung giờ đã tạo
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("thanh cong");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi xảy ra trong quá trình tạo khung giờ: " + e.getMessage());
        }
    }
    @GetMapping("/timeslots/{doctorId}")
    public ResponseEntity<List<TimeslotResponse>> getAvailableTimeSlots(@PathVariable Integer doctorId) {
        try {
            List<TimeslotResponse> timeSlots = timeSlotServices.getAvailableTimeSlots(doctorId);
            return ResponseEntity.ok(timeSlots);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getAlldoctor")
    public ResponseEntity<?> getAllDoctors(@RequestParam(value = "departmentname", required = false) String departmentName) {
        try {
            List<DoctorResponse> doctorResponses;

            if (departmentName != null && !departmentName.isEmpty()) {
                // Nếu có departmentName, tìm bác sĩ theo khoa
                doctorResponses = doctorServices.getAllDoctorsByDepartment(departmentName);
                return ResponseEntity.ok(doctorResponses);
            } else {
                // Nếu không có departmentName, lấy tất cả bác sĩ
                doctorResponses = doctorServices.getAllDoctors();
                return ResponseEntity.ok(doctorResponses);
            }
        } catch (Exception e) {
            // Trả về lỗi với mã 500 nếu có lỗi xảy ra
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching doctors: " + e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable int id) {
        try {
            DoctorResponse doctorResponse = doctorServices.getDoctorById(id);
            return new ResponseEntity<>(doctorResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{id}/ratings")
    public ResponseEntity<List<DoctorRatingResponse>> getRatingsByDoctorId(@PathVariable int id) {
        List<DoctorRatingResponse> ratings = doctorServices.getRatingsByDoctorId(id);
        if (ratings.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ratings, HttpStatus.OK);
    }

    @PutMapping("/updateDoctor")
    public ResponseEntity<String> updateDoctor(@RequestBody @Valid DoctorRequest doctorRequest) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!(authentication instanceof AnonymousAuthenticationToken)) {
                Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
                int doctorIdStr = jwt.getClaim("sub");
                Doctor doctor = doctorRepository.findById(doctorIdStr).orElse(null);

                if (doctor == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bác sĩ không tồn tại");
                }

                // Gọi service để cập nhật thông tin Bác sĩ
                String responseMessage = doctorServices.updateDoctor(doctorRequest,doctor.getDoctorId());

                if (responseMessage.equals("Cập nhật thông tin bác sĩ thành công")) {
                    return ResponseEntity.ok().body(responseMessage);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn cần đăng nhập để thực hiện thao tác này");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating doctor: " + e.getMessage());
        }
    }
    @PostMapping("/admin/create")
    public ResponseEntity<DoctorResponse> addDoctor(@Valid @RequestBody DoctorCreatedDTO doctorDTO) {
        DoctorResponse doctorResponse = doctorServices.addDoctor(doctorDTO);
        return new ResponseEntity<>(doctorResponse, HttpStatus.CREATED);
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<DoctorResponse> updateDoctor(@PathVariable int id,
                                                       @Valid @RequestBody DoctorUpdatedDTO doctorDTO) {
        DoctorResponse doctorResponse = doctorServices.updateDoctor(id, doctorDTO);
        return ResponseEntity.ok(doctorResponse);
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable int id) {
        doctorServices.deleteDoctor(id);
        return ResponseEntity.ok(String.format("Doctor with id %s deleted successfully", id));
    }

}