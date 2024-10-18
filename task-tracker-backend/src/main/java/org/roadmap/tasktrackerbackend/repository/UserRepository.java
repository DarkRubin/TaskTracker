package org.roadmap.tasktrackerbackend.repository;

import org.roadmap.tasktrackerbackend.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findByEmailAndPassword(String email, String password);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
