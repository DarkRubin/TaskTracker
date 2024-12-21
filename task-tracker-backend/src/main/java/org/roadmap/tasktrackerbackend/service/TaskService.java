package org.roadmap.tasktrackerbackend.service;

import lombok.RequiredArgsConstructor;
import org.roadmap.tasktrackerbackend.dto.TaskDTO;
import org.roadmap.tasktrackerbackend.mapper.TaskMapperImpl;
import org.roadmap.tasktrackerbackend.model.Task;
import org.roadmap.tasktrackerbackend.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;
    private final UserService userService;
    private final TaskMapperImpl taskMapper;

    public List<Task> getAll() {
        return repository.getAllByOwner(userService.getCurrentUser());
    }

    public List<Task> getAllFinished() {
        return repository.getAllByOwnerAndFinishedTimeNotNull(userService.getCurrentUser());
    }

    public List<Task> getAllUnfinished() {
        return repository.getAllByOwnerAndFinishedTimeNull(userService.getCurrentUser());
    }

    public List<Task> getAllBetween(Instant from, Instant to) {
        return repository.getAllByOwnerAndFinishedTimeBetween(userService.getCurrentUser(), from, to);
    }

    public Task map(TaskDTO taskDTO) {
        return taskMapper.toEntity(taskDTO);
    }

    public Task save(TaskDTO dto) {
        Task task = map(dto);
        task.setOwner(userService.getCurrentUser());
        return repository.save(task);
    }

    public void delete(TaskDTO dto) {
        repository.deleteByUuidAndOwner(dto.uuid(), userService.getCurrentUser());
    }

    public void updateTitle(TaskDTO taskWithNewTitle) {
        repository.updateTitle(taskWithNewTitle.uuid(), taskWithNewTitle.title());
    }

    public void updateText(TaskDTO taskWithNewText) {
        repository.updateText(taskWithNewText.uuid(), taskWithNewText.text());
    }

    public void updateFinishedTime(UUID uuid, Instant finishedTime) {
        repository.updateFinishedTime(uuid, finishedTime);
    }
}
