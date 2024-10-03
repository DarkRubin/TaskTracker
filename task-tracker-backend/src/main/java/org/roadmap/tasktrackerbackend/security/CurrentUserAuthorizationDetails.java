package org.roadmap.tasktrackerbackend.security;

import lombok.RequiredArgsConstructor;
import org.roadmap.tasktrackerbackend.exception.UserNotAuthorizedException;
import org.roadmap.tasktrackerbackend.model.User;
import org.roadmap.tasktrackerbackend.repository.UserRepository;
import org.springframework.stereotype.Component;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@RequiredArgsConstructor
@Component
public class CurrentUserAuthorizationDetails {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        var email = getContext().getAuthentication().getName();
        var password = getContext().getAuthentication().getCredentials();
        return userRepository.findByEmailAndPassword(email, password.toString())
                .orElseThrow(UserNotAuthorizedException::new);
    }
}
