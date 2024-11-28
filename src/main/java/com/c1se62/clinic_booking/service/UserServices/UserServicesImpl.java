package com.c1se62.clinic_booking.service.UserServices;

import com.c1se62.clinic_booking.dto.request.ForgotPasswordRequest;
import com.c1se62.clinic_booking.dto.request.RegisterRequest;
import com.c1se62.clinic_booking.dto.request.UserRequest;
import com.c1se62.clinic_booking.dto.response.UserResponse;
import com.c1se62.clinic_booking.emuns.ERole;
import com.c1se62.clinic_booking.entity.User;
import com.c1se62.clinic_booking.exception.APIException;
import com.c1se62.clinic_booking.exception.ResourceNotFoundException;
import com.c1se62.clinic_booking.mapper.UserMapper;
import com.c1se62.clinic_booking.repository.AppointmentRepository;
import com.c1se62.clinic_booking.repository.DoctorRatingRepository;
import com.c1se62.clinic_booking.repository.PatientRecordRepository;
import com.c1se62.clinic_booking.repository.UserRepository;
import com.c1se62.clinic_booking.service.SecurityServices.SecurityService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServicesImpl implements UserServices {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    PasswordEncoder passwordEncoder;
    SecurityService securityService;
    DoctorRatingRepository doctorRatingRepository;
    AppointmentRepository appointmentRepository;
    PatientRecordRepository patientRecordRepository;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Override
    public UserResponse register(RegisterRequest request) {
        if(userRepository.existsByUsername(request.getUsername()))
            throw new RuntimeException("ErrorCode.USER_EXISTED");
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<String> roles = new HashSet<>();
        roles.add(ERole.USER.name());
        user.setRole(roles);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public String forgotPassword(ForgotPasswordRequest request, User user) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        // Kiểm tra mật khẩu cũ có đúng không
        boolean authenticated = passwordEncoder.matches(request.getOldPassword(), user.getPassword());

        if (authenticated) {
            // Nếu đúng, cập nhật mật khẩu mới
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);  // Lưu thay đổi vào cơ sở dữ liệu
            return "Mật khẩu thay đổi thành công";
        }

        // Nếu mật khẩu cũ không đúng
        return "Mật khẩu cũ không đúng";
    }

    @Override
    public String updateUser(UserRequest userRequest,Integer userId) {
        Optional<User> existingUser = userRepository.findById(userId);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setFirstName(userRequest.getFirstName());
            user.setLastName(userRequest.getLastName());
            user.setEmail(userRequest.getEmail());
            user.setPhoneNumber(userRequest.getPhoneNumber());
            user.setDateOfBirth(userRequest.getDateOfBirth());
            user.setAddress(userRequest.getAddress());
            user.setCity(userRequest.getCity());
            user.setState(userRequest.getState());
            user.setCountry(userRequest.getCountry());
            user.setBloodgroup(Optional.ofNullable(userRequest.getBloodgroup()).orElse(user.getBloodgroup()));
            user.setZip(Optional.ofNullable(userRequest.getZip()).orElse(user.getZip()));

            userRepository.save(user);

            return "Cập nhật thông tin người dùng thành công";
        } else {
            return "Người dùng không tồn tại";
        }
    }

    @Override
    @Transactional
    public void deleteUser(Integer userId) {
        securityService.validateAdminAccess();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        if (user.getRole().contains(ERole.DOCTOR.name())) {
            throw new APIException(HttpStatus.BAD_REQUEST,
                    "Cannot delete doctor user directly. Please use doctor management endpoints.");
        }
        if (user.getRole().contains(ERole.ADMIN.name())) {
            throw new APIException(HttpStatus.FORBIDDEN,
                    "Cannot delete admin user");
        }
        log.info("Deleting user {} and all related records", userId);
        doctorRatingRepository.deleteByPatientUserId(userId);
        appointmentRepository.deleteByUserUserId(userId);
        patientRecordRepository.deleteByPatientUserId(userId);
        userRepository.delete(user);
        log.info("Successfully deleted user {} and all related records", userId);
    }

    @Override
    public List<UserResponse> getAllUsers(int pageNo, int pageSize, String sortBy, String sortDir) {
        securityService.validateAdminAccess();
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<User> users = userRepository.findAll(pageable);
        return users.getContent()
                .stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse getUserById(Integer userId) {
        securityService.validateAdminAccess();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        return mapToUserResponse(user);
    }

    @Override
    @Transactional
    public void changeUserRole(Integer userId, Set<String> newRoles) {
        securityService.validateAdminAccess();
        for (String role : newRoles) {
            try {
                ERole.valueOf(role);
            } catch (IllegalArgumentException e) {
                throw new APIException(HttpStatus.BAD_REQUEST, "Invalid role: " + role);
            }
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        user.setRole(newRoles);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void toggleUserStatus(Integer userId, boolean isActive) {
        securityService.validateAdminAccess();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        user.setIsActive(isActive);
        userRepository.save(user);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .roles(user.getRole())
                .isActive(user.getIsActive())
                .build();
    }


}
