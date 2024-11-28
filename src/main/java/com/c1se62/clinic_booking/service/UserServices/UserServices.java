package com.c1se62.clinic_booking.service.UserServices;

import com.c1se62.clinic_booking.dto.request.ForgotPasswordRequest;
import com.c1se62.clinic_booking.dto.request.LoginRequest;
import com.c1se62.clinic_booking.dto.request.RegisterRequest;
import com.c1se62.clinic_booking.dto.request.UserRequest;
import com.c1se62.clinic_booking.dto.response.AuthenticationResponse;
import com.c1se62.clinic_booking.dto.response.UserResponse;
import com.c1se62.clinic_booking.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface UserServices {
    UserResponse register(RegisterRequest request) ;
    String forgotPassword(ForgotPasswordRequest request,User users);
    String updateUser(UserRequest user,Integer userId);
    void deleteUser(Integer userId);
    List<UserResponse> getAllUsers(int pageNo, int pageSize, String sortBy, String sortDir);
    UserResponse getUserById(Integer userId);
    void changeUserRole(Integer userId, Set<String> newRoles);
    void toggleUserStatus(Integer userId, boolean isActive);
}
