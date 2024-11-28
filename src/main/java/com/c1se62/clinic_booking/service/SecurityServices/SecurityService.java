package com.c1se62.clinic_booking.service.SecurityServices;

import com.c1se62.clinic_booking.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface SecurityService {
    User getCurrentUser();
    boolean hasRole(String role);
    Integer getCurrentUserId();
    void validateUserAccess(Integer userId);
    void validateDoctorAccess(Integer doctorId);
    void validateAdminAccess();
}
