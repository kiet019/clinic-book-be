package com.c1se62.clinic_booking.service.AppointmentServices;

import com.c1se62.clinic_booking.dto.request.AppointmentRequest;
import com.c1se62.clinic_booking.dto.request.PrescriptionCreateDTO;
import com.c1se62.clinic_booking.dto.response.AppointmentDTO;
import com.c1se62.clinic_booking.dto.response.DoctorDashboardResponse;
import com.c1se62.clinic_booking.entity.*;
import com.c1se62.clinic_booking.exception.APIException;
import com.c1se62.clinic_booking.exception.ResourceNotFoundException;
import com.c1se62.clinic_booking.repository.*;
import com.c1se62.clinic_booking.service.Email.EmailService;
import com.c1se62.clinic_booking.service.SecurityServices.SecurityService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AppointmentServicesImpl implements AppointmentServices{
    AppointmentRepository appointmentRepository;
    TimeSlotRepository timeSlotRepository;
    DoctorRepository doctorRepository;
    EmailService emailService;
    MedicineRepository medicineRepository;
    UserRepository userRepository;
    SecurityService securityService;
    @Override
    public String addAppointment(AppointmentRequest appointment,User user) {
        if (appointment.getDoctorId() == null ||  appointment.getTimeStart() == null ||
                appointment.getBookingDate() == null) {
            try {
                throw new BadRequestException("Invalid appointment request: missing required fields.");
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        }
        Optional<TimeSlot> timeSlotOptional = timeSlotRepository.findByDoctorIdAndDateAndTimeStart(appointment.getDoctorId(), appointment.getTimeStart(), appointment.getBookingDate());
        if (timeSlotOptional.isEmpty()) {
            try {
                throw new BadRequestException("Invalid time slot ID.");
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        }
        TimeSlot timeSlot = timeSlotOptional.get();

        if (timeSlot.getStatus() != TimeSlot.TimeSlotStatus.AVAILABLE) {
            try {
                throw new BadRequestException("Vui lòng đặt vào thời gian khác");
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        }
        Optional<Doctor> doctorOptional = doctorRepository.findById(appointment.getDoctorId());
        if (doctorOptional.isEmpty()) {
            try {
                throw new BadRequestException("Bác sĩ không tồn tại");
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        }
        Doctor doctor = doctorOptional.get();

        Appointment newAppointment = new Appointment();
        newAppointment.setDoctor(doctor);
        newAppointment.setUser(user);
        newAppointment.setTimeSlot(timeSlot);
        newAppointment.setAppointmentType("Khám bệnh");
        newAppointment.setStatus("Đang xử lí");
        Appointment savedAppointment = appointmentRepository.save(newAppointment);
        timeSlot.setStatus(TimeSlot.TimeSlotStatus.BOOKED);
        timeSlotRepository.save(timeSlot);
        String subject = "Xác nhận lịch hẹn khám";
        String body = "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: 'Arial', sans-serif; background-color: #f7f8f9; margin: 0; padding: 0; }" +
                ".container { background-color: #ffffff; margin: 40px auto; padding: 30px; border-radius: 10px; box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1); max-width: 650px; font-size: 16px; line-height: 1.6; }" +
                "h2 { color: #2c3e50; font-size: 24px; margin-bottom: 20px; }" +
                "p { color: #34495e; }" +
                "table { width: 100%; border-collapse: collapse; margin-top: 20px; }" +
                "th, td { border: 1px solid #ecf0f1; padding: 12px; text-align: left; }" +
                "th { background-color: #2980b9; color: #ffffff; font-size: 16px; }" +
                "td { background-color: #f4f6f9; font-size: 15px; color: #34495e; }" +
                ".footer { margin-top: 30px; padding-top: 20px; border-top: 1px solid #ecf0f1; text-align: center; font-size: 14px; color: #95a5a6; }" +
                ".footer a { color: #2980b9; text-decoration: none; }" +
                ".header { text-align: center; padding-bottom: 20px; border-bottom: 2px solid #ecf0f1; margin-bottom: 30px; }" +
                ".header img { width: 80px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<img src='https://png.pngtree.com/png-clipart/20230427/original/pngtree-medical-logo-png-image_9116167.png' alt='Clinic Logo' />" +
                "</div>" +
                "<h2>Chào " + user.getFirstName() + ",</h2>" +
                "<p>Chúng tôi xin thông báo rằng lịch hẹn khám của bạn đã được xác nhận thành công. Dưới đây là thông tin chi tiết về lịch hẹn của bạn:</p>" +
                "<table>" +
                "<tr><th>Bệnh nhân</th><td>" + user.getFirstName() + " " + user.getLastName() + "</td></tr>" +
                "<tr><th>Bác sĩ</th><td>" + doctor.getUser().getFirstName() + " " + doctor.getUser().getLastName() + "</td></tr>" +
                "<tr><th>Mã cuộc hẹn</th><td>" + newAppointment.getAppointmentId() + "</td></tr>" +
                "<tr><th>Thời gian</th><td>" + appointment.getBookingDate() + " lúc " + appointment.getTimeStart() + "</td></tr>" +
                "</table>" +
                "<p class='footer'>Chúng tôi hy vọng được chào đón bạn tại phòng khám. Nếu bạn có bất kỳ câu hỏi nào hoặc cần thay đổi lịch hẹn, vui lòng liên hệ với chúng tôi.</p>" +
                "<p class='footer'>Trân trọng,<br>Đội ngũ Clinic</p>" +
                "<p class='footer'><a href='mailto:support@clinic.com'>Liên hệ với chúng tôi</a> | <a href='#'>Trang web phòng khám</a></p>" +
                "</div>" +
                "</body>" +
                "</html>";

        try {
            emailService.sendHtmlEmail(user.getEmail(), subject, body);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return "Appointment created successfully! Appointment ID: " + savedAppointment.getAppointmentId();
    }

    @Override
    public boolean addPrescriptions(List<PrescriptionCreateDTO> prescriptions, Integer appointmentId) {
        if (!securityService.hasRole("ROLE_DOCTOR"))
            throw new APIException(HttpStatus.FORBIDDEN, "You are not allowed to add prescriptions.");
        User user = securityService.getCurrentUser();
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(
                () -> new ResourceNotFoundException("Appointment", "Id", appointmentId));
        if (!appointment.getDoctor().getDoctorId().equals(user.getUserId()))
            throw new APIException(HttpStatus.FORBIDDEN, "You are not allowed to add prescriptions to this appointment.");
        if (prescriptions.isEmpty())
            throw new APIException(HttpStatus.BAD_REQUEST, "Prescription list is empty.");
        if (!appointment.getStatus().equals("COMPLETED"))
            throw new APIException(HttpStatus.BAD_REQUEST, "Appointment is not completed yet.");
        List<Medicine> medicines = medicineRepository.findByIds(prescriptions.stream()
                .map(PrescriptionCreateDTO::getMedicineId).toList());
        Map<Integer, Medicine> medicineMap = medicines.stream().collect(
                Collectors.toMap(Medicine::getMedicineId, medicine -> medicine));
        if (medicines.size() != prescriptions.size())
            throw new APIException(HttpStatus.BAD_REQUEST, "Some medicines are not found.");
        Set<Prescription> prescriptionsList = prescriptions.stream()
                .map(prescriptionCreateDTO ->
                        mapToEntity(prescriptionCreateDTO, medicineMap.get(prescriptionCreateDTO.getMedicineId())))
                .collect(Collectors.toSet());
        appointment.getPrescriptions().addAll(prescriptionsList);
        appointmentRepository.save(appointment);
        return true;
    }

    @Override
    public List<AppointmentDTO> getAppointmentsByUserId(Integer userId) {
        // Lấy danh sách lịch khám từ repository
        List<Appointment> appointments = appointmentRepository.findByUserId(userId);

        // Chuyển đổi sang DTO
        return appointments.stream().map(a -> new AppointmentDTO(
                a.getAppointmentId(),
                a.getTimeSlot().getDate(),
                a.getTimeSlot().getTimeStart(),
                a.getTimeSlot().getTimeEnd(),
                a.getDoctor().getUser().getFirstName(),
                a.getStatus().toString()
        )).collect(Collectors.toList());
    }

    @Override
    public List<DoctorDashboardResponse> getAppointmentsByDoctorId(Integer doctorId) {
        // Lấy danh sách lịch khám từ repository lên doctor Dashboard
        return appointmentRepository.findAppointmentsByDoctorId(doctorId);
    }

    private Prescription mapToEntity(PrescriptionCreateDTO prescriptionCreateDTO, Medicine medicine) {
        Prescription prescription = new Prescription();
        prescription.setMedicine(medicine);
        prescription.setDosage(prescriptionCreateDTO.getDosage());
        return prescription;
    }
}
