package org.roadmap.tasktrackerbackend.service;

import lombok.RequiredArgsConstructor;
import org.roadmap.tasktrackerbackend.dto.TaskDTO;
import org.roadmap.tasktrackerbackend.exception.TaskNotFoundException;
import org.roadmap.tasktrackerbackend.mapper.TaskMapper;
import org.roadmap.tasktrackerbackend.model.Task;
import org.roadmap.tasktrackerbackend.repository.TaskRepository;
import org.roadmap.tasktrackerbackend.security.CurrentUserAuthorizationDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;
    private final CurrentUserAuthorizationDetails details;
    private final TaskMapper taskMapper;

    public List<Task> getAll() {
        return repository.getAllByOwner(details.getCurrentUser());
    }

    public List<Task> getAllFinished() {
        return repository.getAllByOwnerAndFinishedTimeNotNull(details.getCurrentUser());
    }

    public List<Task> getAllUnfinished() {
        return repository.getAllByOwnerAndFinishedTimeNull(details.getCurrentUser());
    }

    public List<Task> getAllBetween(Instant from, Instant to) {
        return repository.getAllByOwnerAndFinishedTimeBetween(details.getCurrentUser(), from, to);
    }

    public Task map(TaskDTO taskDTO) {
        return taskMapper.toEntity(taskDTO);
    }

    public Task update(Task task) {
        task.setOwner(details.getCurrentUser());
        return repository.save(task);
    }

    public Task getTaskByUuid(UUID id) {
        return repository.findByUuidAndOwner(id, details.getCurrentUser()).orElseThrow(TaskNotFoundException::new);
    }

    public void delete(TaskDTO dto) {
        Task task = map(dto);
        task.setOwner(details.getCurrentUser());
        repository.delete(task);
    }

    public Task save(TaskDTO dto) {
        Task task = map(dto);
        task.setOwner(details.getCurrentUser());
        return repository.save(task);
    }

}
