package com.lottofun;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lottofun.dto.request.UserRegisterRequest;
import com.lottofun.dto.request.UserLoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerIT {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void should_register_user_successfully() throws Exception {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setUsername("admin");
        request.setEmail("admin@gmail.com");
        request.setPassword("password");


        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.email").value("admin@gmail.com"));
    }

    @Test
    void should_fail_register_when_email_exists() throws Exception {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setUsername("admin");
        request.setEmail("admin@gmail.com");
        request.setPassword("password");
        // İlk kayıt başarılı
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Aynı email ile ikinci deneme
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict()); // veya .isBadRequest()
    }

    @Test
    void should_login_successfully() throws Exception {
        // Önce kayıt ol
        UserRegisterRequest register = new UserRegisterRequest();
        register.setUsername("admin");
        register.setEmail("admin@gmail.com");
        register.setPassword("password");
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isOk());

        // Sonra login
        UserLoginRequest login = new UserLoginRequest();
        login.setEmail("admin@gmail.com");
        login.setPassword("password");

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void should_fail_login_with_wrong_password() throws Exception {
        // Kayıt
        UserRegisterRequest register = new UserRegisterRequest();
        register.setUsername("admin");
        register.setEmail("admin@gmail.com");
        register.setPassword("password");
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isOk());

        // Hatalı şifre
        UserLoginRequest login = new UserLoginRequest();
        login.setEmail("admin@gmail.com");
        login.setPassword("wrong-password");

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized());
    }
}
