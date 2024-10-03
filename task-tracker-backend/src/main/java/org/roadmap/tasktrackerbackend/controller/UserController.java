package org.roadmap.tasktrackerbackend.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.roadmap.tasktrackerbackend.dto.UserDTO;
import org.roadmap.tasktrackerbackend.exception.EmailAlreadyTakenException;
import org.roadmap.tasktrackerbackend.exception.InvalidUserDetailsException;
import org.roadmap.tasktrackerbackend.exception.PasswordsDoNotMatchException;
import org.roadmap.tasktrackerbackend.exception.UserNotFoundException;
import org.roadmap.tasktrackerbackend.model.User;
import org.roadmap.tasktrackerbackend.repository.UserRepository;
import org.roadmap.tasktrackerbackend.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository repository;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/user")
    public User getUser() {
        return repository.findByEmail(SecurityContextHolder.getContext()
                        .getAuthentication().getName())
                .orElseThrow(UserNotFoundException::new);
    }

    @PostMapping(value = "/user",
            consumes = {"application/x-www-form-urlencoded;charset=UTF-8", "application/json"})
    public void registerUser(@Valid UserDTO dto, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            throw new InvalidUserDetailsException();
        }
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new PasswordsDoNotMatchException();
        }
        if (repository.findByEmail(dto.getEmail()).isPresent()) {
            throw new EmailAlreadyTakenException();
        }
        String token = jwtService.generateToken(dto.getEmail(), dto.getPassword());
        response.addHeader("Authorization", "Bearer " + token);
        response.addCookie(new Cookie("Access-Token", token));
    }

    @PostMapping(value = "/auth/login",
            consumes = {"application/x-www-form-urlencoded;charset=UTF-8", "application/json"})
    public void login(String email, String password, HttpServletResponse response) {
        var authentication = new UsernamePasswordAuthenticationToken(email, password);
        authenticationManager.authenticate(authentication);
        User user = repository.getByEmail(email);
        String token = jwtService.generateToken(user);
        String token = jwtService.generateToken(email, encodedPassword);
        response.addHeader("Authorization", "Bearer " + token);
        response.addCookie(new Cookie("Access-Token", token));
    }

}
