package org.roadmap.tasktrackerbackend.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.roadmap.tasktrackerbackend.dto.UserDTO;
import org.roadmap.tasktrackerbackend.exception.EmailAlreadyTakenException;
import org.roadmap.tasktrackerbackend.exception.InvalidUserDetailsException;
import org.roadmap.tasktrackerbackend.exception.UserNotFoundException;
import org.roadmap.tasktrackerbackend.model.User;
import org.roadmap.tasktrackerbackend.repository.UserRepository;
import org.roadmap.tasktrackerbackend.security.CurrentUserAuthorizationDetails;
import org.roadmap.tasktrackerbackend.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository repository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final CurrentUserAuthorizationDetails details;

    @GetMapping("/user")
    public User getUser() {
        return details.getCurrentUser();
    }

    @PostMapping(value = "/user", consumes = "application/*")
    public void registerUser(@Valid @RequestBody UserDTO dto, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            throw new InvalidUserDetailsException();
        }
        String email = dto.email();
        String password = dto.password();
        if (repository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyTakenException();
        }
        String encoded = passwordEncoder.encode(password);
        repository.save(new User(email, encoded));
        String token = jwtService.generateToken(email, encoded);
        response.addHeader("Authorization", token);
    }

    @PostMapping(value = "/auth/login", consumes = "application/*")
    public void login(@RequestBody UserDTO userDTO, HttpServletResponse response) {
        String email = userDTO.email();
        String password = userDTO.password();
        var authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            authenticationManager.authenticate(authentication);
        } catch (AuthenticationException e) {
            throw new UserNotFoundException();
        }
        String encodedPassword = passwordEncoder.encode(password);
        String token = jwtService.generateToken(email, encodedPassword);
        response.addHeader("Authorization", token);
    }

    @DeleteMapping("/auth/logout")
    public void logout(HttpServletResponse response) {
        response.setHeader("Authorization", "");
        SecurityContextHolder.clearContext();
    }

}
