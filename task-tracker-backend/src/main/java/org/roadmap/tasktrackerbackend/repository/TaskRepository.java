package org.roadmap.tasktrackerbackend.repository;

import org.roadmap.tasktrackerbackend.model.Task;
import org.roadmap.tasktrackerbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> getAllByOwner(User owner);

    Optional<Task> findByUuidAndOwner(UUID uuid, User owner);

    List<Task> getAllByOwnerAndFinishedTimeNull(User owner);

    List<Task> getAllByOwnerAndFinishedTimeNotNull(User owner);

    List<Task> getAllByOwnerAndFinishedTimeBetween(User owner, Instant finishedTimeAfter, Instant finishedTimeBefore);
}
