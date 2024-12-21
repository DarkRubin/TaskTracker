package org.roadmap.tasktrackerbackend.repository;

import jakarta.transaction.Transactional;
import org.roadmap.tasktrackerbackend.model.Task;
import org.roadmap.tasktrackerbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    @Transactional
    @Modifying
    @Query("update Task t set t.finishedTime = ?2 where t.uuid = ?1")
    void updateFinishedTime(UUID uuid, Instant finishedTime);

    @Transactional
    @Modifying
    @Query("update Task t set t.text = ?2 where t.uuid = ?1")
    void updateText(UUID uuid, String text);

    @Transactional
    @Modifying
    @Query("update Task t set t.title = ?2 where t.uuid = ?1")
    void updateTitle(UUID uuid, String title);

    void deleteByUuidAndOwner(UUID uuid, User owner);

    List<Task> getAllByOwner(User owner);

    Optional<Task> findByUuidAndOwner(UUID uuid, User owner);

    List<Task> getAllByOwnerAndFinishedTimeNull(User owner);

    List<Task> getAllByOwnerAndFinishedTimeNotNull(User owner);

    List<Task> getAllByOwnerAndFinishedTimeBetween(User owner, Instant finishedTimeAfter, Instant finishedTimeBefore);
}
