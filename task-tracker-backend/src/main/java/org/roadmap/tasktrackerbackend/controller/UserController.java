package org.roadmap.tasktrackerbackend.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.roadmap.tasktrackerbackend.dto.UserDTO;
import org.roadmap.tasktrackerbackend.kafka.KafkaProducer;
import org.roadmap.tasktrackerbackend.model.User;
import org.roadmap.tasktrackerbackend.service.JwtService;
import org.roadmap.tasktrackerbackend.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    JwtService jwtService;
    KafkaProducer producer;
    UserService userService;
    PasswordEncoder passwordEncoder;

    @GetMapping("/user")
    public User getUser(@AuthenticationPrincipal User user) {
        return user;
    }

    @PostMapping("/user")
    public void registerUser(@Valid @RequestBody UserDTO dto, HttpServletResponse response) {
        String email = dto.email();
        String password = passwordEncoder.encode(dto.password());
        userService.createNewUser(email, password);
        String token = jwtService.generateToken(email, password);
        response.addHeader("Authorization", token);
        producer.sendSuccessRegistration(email);
    }

    @PostMapping("/auth/login")
    public void login(@RequestBody UserDTO userDTO, HttpServletResponse response) {
        String token = jwtService.generateToken(userDTO.email(), userDTO.password());
        response.addHeader("Authorization", token);
    }

    @DeleteMapping("/auth/logout")
    public void logout(HttpServletResponse response) {
        response.setHeader("Authorization", "");
        SecurityContextHolder.clearContext();
    }

}
