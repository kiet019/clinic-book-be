package com.c1se62.clinic_booking.service.DoctorServices;

import com.c1se62.clinic_booking.dto.request.DoctorCreatedDTO;
import com.c1se62.clinic_booking.dto.request.DoctorRequest;
import com.c1se62.clinic_booking.dto.request.DoctorUpdatedDTO;
import com.c1se62.clinic_booking.dto.request.UserRequest;
import com.c1se62.clinic_booking.dto.response.DoctorRatingResponse;
import com.c1se62.clinic_booking.dto.response.DoctorResponse;
import com.c1se62.clinic_booking.emuns.ERole;
import com.c1se62.clinic_booking.entity.Department;
import com.c1se62.clinic_booking.entity.Doctor;
import com.c1se62.clinic_booking.entity.DoctorRating;
import com.c1se62.clinic_booking.entity.User;
import com.c1se62.clinic_booking.repository.DepartmentRepository;
import com.c1se62.clinic_booking.repository.DoctorRatingRepository;
import com.c1se62.clinic_booking.repository.DoctorRepository;
import com.c1se62.clinic_booking.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DoctorServicesImpl implements DoctorServices {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private DoctorRatingRepository doctorRatingRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    @Override
    public List<DoctorResponse> getAllDoctors() throws Exception {
        List<Doctor> doctors = doctorRepository.getAllDoctor();
        return doctors.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorResponse> getAllDoctorsByDepartment(String departmentName) throws Exception {
        Department department = departmentRepository.findByName(departmentName);
        if (department == null) {
            throw new Exception("Department with name '" + departmentName + "' not found.");
        }

        List<Doctor> doctors = doctorRepository.getAllDoctorByDepartment(department.getDepartmentId());
        return doctors.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DoctorResponse getDoctorById(int id) throws Exception {
        return doctorRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new Exception("Doctor not found with id: " + id));
    }

    @Override
    public DoctorResponse updateDoctor(int id, DoctorUpdatedDTO doctorDTO) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));

        if (doctorDTO.getBio() != null) {
            doctor.setBio(doctorDTO.getBio());
        }
        if (doctorDTO.getSpeciality() != null) {
            doctor.setSpeciality(doctorDTO.getSpeciality());
        }

        if (doctorDTO.getDepartmentId() != null) {
            Department department = departmentRepository.findById(doctorDTO.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            doctor.setDepartment(department);
        }

        User user = doctor.getUser();
        if (doctorDTO.getFirstName() != null) {
            user.setFirstName(doctorDTO.getFirstName());
        }
        if (doctorDTO.getLastName() != null) {
            user.setLastName(doctorDTO.getLastName());
        }
        if (doctorDTO.getEmail() != null) {
            user.setEmail(doctorDTO.getEmail());
        }
        if (doctorDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(doctorDTO.getPhoneNumber());
        }
        if (doctorDTO.getAddress() != null) {
            user.setAddress(doctorDTO.getAddress());
        }
        if (doctorDTO.getCity() != null) {
            user.setCity(doctorDTO.getCity());
        }
        if (doctorDTO.getState() != null) {
            user.setState(doctorDTO.getState());
        }
        if (doctorDTO.getZip() != null) {
            user.setZip(doctorDTO.getZip());
        }
        if (doctorDTO.getCountry() != null) {
            user.setCountry(doctorDTO.getCountry());
        }
        if (doctorDTO.getDateOfBirth() != null) {
            user.setDateOfBirth(doctorDTO.getDateOfBirth());
        }
        if (doctorDTO.getBloodgroup() != null) {
            user.setBloodgroup(doctorDTO.getBloodgroup());
        }
        userRepository.save(user);
        Doctor updatedDoctor = doctorRepository.save(doctor);
        return mapToDTO(updatedDoctor);
    }

    @Override
    public DoctorResponse addDoctor(DoctorCreatedDTO doctorDTO) {
        User user = new User();
        user.setUsername(doctorDTO.getUsername());
        user.setPassword(passwordEncoder.encode(doctorDTO.getPassword()));
        user.setFirstName(doctorDTO.getFirstName());
        user.setLastName(doctorDTO.getLastName());
        user.setEmail(doctorDTO.getEmail());
        user.setPhoneNumber(doctorDTO.getPhoneNumber());

        HashSet<String> roles = new HashSet<>();
        roles.add(ERole.DOCTOR.name());
        user.setRole(roles);

        User savedUser = userRepository.save(user);

        Department department = departmentRepository.findById(doctorDTO.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Doctor doctor = new Doctor();
        doctor.setUser(savedUser);
        doctor.setDepartment(department);
        doctor.setBio(doctorDTO.getBio());
        doctor.setSpeciality(doctorDTO.getSpeciality());
        Doctor savedDoctor = doctorRepository.save(doctor);

        DoctorResponse response = new DoctorResponse();
        response.setDoctorId(savedDoctor.getDoctorId());
        response.setDoctorName(savedDoctor.getUser().getFirstName() + " " + savedDoctor.getUser().getLastName());
        response.setBio(savedDoctor.getBio());
        response.setSpeciality(savedDoctor.getSpeciality());
        response.setDepartment(savedDoctor.getDepartment().getName());
        response.setRating(0.0);

        return response;
    }


    @Override
    public String updateDoctor(DoctorRequest doctorRequest, Integer doctorId) {
        Optional<Doctor> existingDoctor = doctorRepository.findById(doctorId);
        if (existingDoctor.isPresent()) {
            Doctor d = existingDoctor.get();
            d.getUser().setUsername(doctorRequest.getDoctorName());
            d.getUser().setFirstName(doctorRequest.getFirstName());
            d.getUser().setLastName(doctorRequest.getLastName());
            d.getUser().setEmail(doctorRequest.getEmail());
            d.getUser().setPhoneNumber(doctorRequest.getPhoneNumber());
            d.getUser().setDateOfBirth(doctorRequest.getDateOfBirth());
            doctorRepository.save(d);
            return "Update doctor successfully";
        } else {
            return "Doctor not found";
        }
    }

    @Override
    public List<DoctorRatingResponse> getRatingsByDoctorId(int doctorId) {
        List<DoctorRating> ratings = doctorRatingRepository.findByDoctor(doctorId);
        return ratings.stream()
                .map(rating -> new DoctorRatingResponse(rating.getRatingId(), rating.getRating(), rating.getComment(), rating.getCreatedAt()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteDoctor(int id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
        doctorRepository.delete(doctor);
        userRepository.delete(doctor.getUser());
    }

    private DoctorResponse mapToDTO(Doctor doctor) {
        DoctorResponse response = new DoctorResponse();
        response.setDoctorId(doctor.getDoctorId());
        response.setBio(doctor.getBio());
        response.setSpeciality(doctor.getSpeciality());
        response.setDoctorName(doctor.getUser() != null ?
                doctor.getUser().getFirstName() + " " + doctor.getUser().getLastName() : "Unknown");
        response.setDepartment(doctor.getDepartment() != null ?
                doctor.getDepartment().getName() : "Unknown");

        Double averageRating = doctorRatingRepository.findAverageRatingByDoctorId(doctor.getDoctorId());
        response.setRating(averageRating != null ? averageRating : 0.0);

        return response;
    }
}
