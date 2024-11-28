package com.c1se62.clinic_booking.service.SecurityServices;

import com.c1se62.clinic_booking.entity.User;
import com.c1se62.clinic_booking.exception.APIException;
import com.c1se62.clinic_booking.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SecurityServiceImpl implements SecurityService{
    UserRepository userRepository;
    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
            String userNameString = jwt.getClaim("sub");
            User user = userRepository.findByUsername(userNameString).orElse(null);
            if (user != null) {
                return user;
            }
        }
        throw new APIException(HttpStatus.UNAUTHORIZED, "User not authenticated");

    }

    @Override
    public boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
                authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public Integer getCurrentUserId() {
        return getCurrentUser().getUserId();
    }

    @Override
    public void validateUserAccess(Integer userId) {
        if (!getCurrentUserId().equals(userId) && !hasRole("ADMIN")) {
            throw new APIException(HttpStatus.FORBIDDEN, "Access denied");
        }
    }

    @Override
    public void validateDoctorAccess(Integer doctorId) {
        if (!getCurrentUserId().equals(doctorId) && !hasRole("ADMIN")) {
            throw new APIException(HttpStatus.FORBIDDEN, "Only doctor or admin can access this resource");
        }
    }

    @Override
    public void validateAdminAccess() {
        if (!hasRole("ADMIN")) {
            throw new APIException(HttpStatus.FORBIDDEN, "Only admin can access this resource");
        }
    }
}
