package org.roadmap.tasktrackerbackend.repository;

import org.roadmap.tasktrackerbackend.model.Task;
import org.roadmap.tasktrackerbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> getAllByOwner(User owner);

    @Query("from Task where owner =:owner and finishedTime != null")
    List<Task> getAllFinishedByOwner(User owner);

    @Transactional
    void deleteByUuid(UUID uuid);

    @Transactional
    @Modifying
    @Query("update Task set finishedTime = :finishedTime where uuid = :uuid")
    void updateFinishedTimeByUuid(UUID uuid, Instant finishedTime);

    @Transactional
    @Modifying
    @Query("update Task set title = :title where uuid = :uuid")
    void updateTitleByUuid(UUID uuid, String title);

    @Transactional
    @Modifying
    @Query("update Task set text = :text where uuid = :uuid")
    void updateTextByUuid(UUID uuid, String text);
}
