package org.roadmap.tasktrackerscheduler.repository;


import org.roadmap.tasktrackerscheduler.entity.Task;
import org.roadmap.tasktrackerscheduler.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> getAllByOwner(User owner);

    List<Task> getAllByOwnerAndFinishedTimeNull(User owner);

    List<Task> getAllByOwnerAndFinishedTimeNotNull(User owner);

}
