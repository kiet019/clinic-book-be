package com.c1se62.clinic_booking.service.PrescriptionServices;

import com.c1se62.clinic_booking.dto.response.PrescriptionDTO;
import com.c1se62.clinic_booking.dto.response.PrescriptionResponseDTO;
import com.c1se62.clinic_booking.entity.Appointment;
import com.c1se62.clinic_booking.entity.Prescription;
import com.c1se62.clinic_booking.repository.AppointmentRepository;
import com.c1se62.clinic_booking.repository.MedicineRepository;
import com.c1se62.clinic_booking.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class PrescriptionServiceImpl implements PrescriptionServices {
    @Autowired
    private PrescriptionRepository prescriptionRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private MedicineRepository medicineRepository;

    @Override
    public PrescriptionResponseDTO getPrescriptionByAppointmentId(int appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).get();
        PrescriptionResponseDTO responseDTO = new PrescriptionResponseDTO();
        responseDTO.setUsername(appointment.getUser().getFirstName() + " " + appointment.getUser().getLastName());
        responseDTO.setEmail(appointment.getUser().getEmail());
        responseDTO.setPhoneNumber(appointment.getUser().getPhoneNumber());
        responseDTO.setAddress(appointment.getUser().getAddress());
        responseDTO.setCity(appointment.getUser().getCity());
        responseDTO.setState(appointment.getUser().getState());
        responseDTO.setZip(appointment.getUser().getZip());
        responseDTO.setCountry(appointment.getUser().getCountry());
        responseDTO.setDateOfBirth(appointment.getUser().getDateOfBirth());
        responseDTO.setDoctorName(appointment.getDoctor().getUser().getFirstName() + " " + appointment.getDoctor().getUser().getLastName()); // Assuming Doctor has firstName and lastName
        responseDTO.setDepartmentName(appointment.getDoctor().getDepartment().getName()); // Assuming Doctor belongs to a Department with a name
        responseDTO.setDate(appointment.getTimeSlot().getDate());
        responseDTO.setTimeStart(appointment.getTimeSlot().getTimeStart());
        responseDTO.setTimeEnd(appointment.getTimeSlot().getTimeEnd());
        Set<Prescription> prescriptions = appointment.getPrescriptions();
        Set<PrescriptionDTO> prescriptionDTOS = new HashSet<>();
        for(Prescription prescription : prescriptions){
            PrescriptionDTO prescriptionDTO=new PrescriptionDTO();
        prescriptionDTO.setDosage(prescription.getDosage());
        prescriptionDTO.setName(prescription.getMedicine().getName());
        prescriptionDTO.setDescription(prescription.getMedicine().getDescription());
        prescriptionDTO.setStock(prescription.getMedicine().getStock());
        prescriptionDTOS.add(prescriptionDTO);
        }
        responseDTO.setPrescriptionDTO(prescriptionDTOS);
        return responseDTO;
    }
}
