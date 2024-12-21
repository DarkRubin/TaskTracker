package org.roadmap.tasktrackerbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.roadmap.tasktrackerbackend.exception.EmailAlreadyTakenException;
import org.roadmap.tasktrackerbackend.exception.UserNotFoundException;
import org.roadmap.tasktrackerbackend.model.User;
import org.roadmap.tasktrackerbackend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = repository.findByEmail(username)
                .orElseThrow(UserNotFoundException::new);
        return org.springframework.security.core.userdetails.User
                .withUsername(username).password(user.getPassword()).build();
    }

    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Transactional
    public void createNewUser(String email, String password) {
        if (repository.existsByEmail(email)) {
            throw new EmailAlreadyTakenException();
        }
        repository.save(new User(email, password));
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        return repository.findByEmailAndPassword(username, password)
                .orElseThrow(UserNotFoundException::new);
    }
}
