package org.roadmap.tasktrackerbackend.repository;

import org.roadmap.tasktrackerbackend.exception.TaskNotFoundException;
import org.roadmap.tasktrackerbackend.model.Task;
import org.roadmap.tasktrackerbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> getAllByOwner(User owner);

    Optional<Task> findByUuidAndOwner(UUID uuid, User owner);

    default Task getTaskByUuidAndOwnerOrThrow(UUID id, User owner) {
        return findByUuidAndOwner(id, owner).orElseThrow(TaskNotFoundException::new);
    }

    @Query("from Task where owner =:owner and finishedTime != null")
    List<Task> getAllFinishedByOwner(User owner);
}
