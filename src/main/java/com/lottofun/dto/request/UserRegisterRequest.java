package com.lottofun.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterRequest {

    @NotBlank(message = "Kullanıcı adı boş olamaz")
    private String username;

    @NotBlank(message = "E-posta boş olamaz")
    @Email(message = "Geçerli bir e-posta giriniz")
    private String email;

    @NotBlank(message = "Şifre boş olamaz")
    @Size(min = 6, message = "Şifre en az 6 karakter olmalıdır")
    private String password;


}