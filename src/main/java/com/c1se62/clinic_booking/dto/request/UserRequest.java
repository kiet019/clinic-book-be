package com.c1se62.clinic_booking.dto.request;

import jakarta.persistence.ElementCollection;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import java.util.Date;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRequest {
    @NotNull(message = "Vui lòng nhập họ của bạn.")
    private String firstName;

    @NotNull(message = "Vui lòng nhập tên của bạn.")
    private String lastName;

    @NotNull(message = "Vui lòng nhập địa chỉ email hợp lệ.")
    @Email
    private String email;

    @NotNull(message = "Vui lòng nhập số điện thoại.")
    @Pattern(regexp = "^\\d+$", message = "Số điện thoại chỉ được chứa các chữ số.")
    private String phoneNumber;

    @NotNull(message = "Vui lòng nhập địa chỉ của bạn.")
    private String address;

    @NotNull(message = "Vui lòng chọn thành phố.")
    private String city;

    @NotNull(message = "Vui lòng chọn tỉnh/thành phố.")
    private String state;

    private String zip;

    @NotNull(message = "Vui lòng chọn quốc gia.")
    private String country;

    @NotNull(message = "Vui lòng nhập ngày sinh.")
    @Past
    private Date dateOfBirth;

    @NotNull(message = "Vui lòng chọn nhóm máu.")
    private String bloodgroup;
}
