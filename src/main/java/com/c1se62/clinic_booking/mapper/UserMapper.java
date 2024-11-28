package com.c1se62.clinic_booking.mapper;

import com.c1se62.clinic_booking.dto.request.RegisterRequest;
import com.c1se62.clinic_booking.dto.response.UserResponse;
import com.c1se62.clinic_booking.entity.User;
import org.mapstruct.Mapper;

@Mapper (componentModel = "spring")
public interface    UserMapper {
    User toUser(RegisterRequest request);
    UserResponse toUserResponse(User user);
}
