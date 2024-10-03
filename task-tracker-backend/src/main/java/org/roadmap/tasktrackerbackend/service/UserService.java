package org.roadmap.tasktrackerbackend.service;

import lombok.RequiredArgsConstructor;
import org.roadmap.tasktrackerbackend.exception.UserNotFoundException;
import org.roadmap.tasktrackerbackend.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
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
        var user = repository.findByEmail(username).orElseThrow(UserNotFoundException::new);
        return User.withUsername(username).password(user.getPassword()).build();
    }
}
