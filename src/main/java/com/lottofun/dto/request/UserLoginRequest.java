package com.lottofun.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequest {

    @NotBlank(message = "E-posta boş olamaz")
    @Email
    private String email;

    @NotBlank(message = "Şifre boş olamaz")
    private String password;


}
