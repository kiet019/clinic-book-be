package com.c1se62.clinic_booking.dto.request;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotNull(message = "Vui lòng nhập mật khẩu và tên đăng nhập")
    private String username;
    @NotNull(message = "Vui lòng nhập mật khẩu và tên đăng nhập")
    private String password;
}
