package org.roadmap.tasktrackerbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.roadmap.tasktrackerbackend.dto.UserDTO;
import org.roadmap.tasktrackerbackend.exception.EmailAlreadyTakenException;
import org.roadmap.tasktrackerbackend.exception.InvalidUserDetailsException;
import org.roadmap.tasktrackerbackend.exception.PasswordsDoNotMatchException;
import org.roadmap.tasktrackerbackend.exception.UserNotFoundException;
import org.roadmap.tasktrackerbackend.model.User;
import org.roadmap.tasktrackerbackend.repository.UserRepository;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository repository;

    @GetMapping(value = "/user",
            consumes = {"application/x-www-form-urlencoded;charset=UTF-8", "application/json"}, produces = "application/json")
    public User getUser(@RequestBody MultiValueMap<String, String> model) {
        return repository.findByEmailAndPassword(model.getFirst("email"), model.getFirst("password"))
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @PostMapping(value = "/user",
            consumes = {"application/x-www-form-urlencoded;charset=UTF-8", "application/json"})
    public void registerUser(@Valid UserDTO user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidUserDetailsException("Invalid input data");
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new PasswordsDoNotMatchException("Passwords do not match");
        }
        if (repository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyTakenException("This email is already taken");
        }
        repository.save(new User(user.getEmail(), user.getPassword()));
    }

}
