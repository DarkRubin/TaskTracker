package org.roadmap.tasktrackerbackend.repository;

import org.roadmap.tasktrackerbackend.model.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Long> {
}
