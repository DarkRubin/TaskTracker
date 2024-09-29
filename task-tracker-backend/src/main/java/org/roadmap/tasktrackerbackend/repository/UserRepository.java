package org.roadmap.tasktrackerbackend.repository;

import org.roadmap.tasktrackerbackend.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {

    User getByEmailAndPassword(String email, String password);

    Optional<User> findByEmailAndPassword(String email, String password);

    User getByEmail(String email);

    Optional<User> findByEmail(String email);
}
