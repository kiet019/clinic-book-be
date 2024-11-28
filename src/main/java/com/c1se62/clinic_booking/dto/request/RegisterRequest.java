package com.c1se62.clinic_booking.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterRequest {
    @NotNull(message = "Tên đăng nhập không được để trống. Vui lòng nhập tên đăng nhập.")
    @Size(min = 3, message = "Tên đăng nhập phải có ít nhất 3 ký tự.")
    private String username;

    @NotNull(message = "Mật khẩu không được để trống. Vui lòng nhập mật khẩu.")
    @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự.")
    private String password;

    private Set<String> role;

    @NotNull(message = "Vui lòng nhập tên của bạn.")
    @Size(min = 2, message = "Tên phải có ít nhất 2 ký tự.")
    private String firstName;

    @NotNull(message = "Vui lòng nhập họ của bạn.")
    @Size(min = 2, message = "Họ phải có ít nhất 2 ký tự.")
    private String lastName;

    @NotNull(message = "Vui lòng nhập địa chỉ email hợp lệ.")
    @Email(message = "Email không hợp lệ. Vui lòng nhập địa chỉ email đúng định dạng.")
    private String email;

    @NotNull(message = "Vui lòng nhập số điện thoại.")
    @Size(min = 10, message = "Số điện thoại phải có ít nhất 10 chữ số.")
    @Pattern(regexp = "^[0-9]{10,}$", message = "Số điện thoại phải chỉ chứa các chữ số và ít nhất 10 chữ số.")
    private String phoneNumber;
}

