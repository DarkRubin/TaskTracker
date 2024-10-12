package org.roadmap.tasktrackerbackend.controller;

import lombok.RequiredArgsConstructor;
import org.roadmap.tasktrackerbackend.dto.TaskDTO;
import org.roadmap.tasktrackerbackend.exception.InvalidDateParameterException;
import org.roadmap.tasktrackerbackend.mapper.TaskMapper;
import org.roadmap.tasktrackerbackend.model.Task;
import org.roadmap.tasktrackerbackend.repository.TaskRepository;
import org.roadmap.tasktrackerbackend.security.CurrentUserAuthorizationDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskRepository repository;

    private final CurrentUserAuthorizationDetails details;
    private final TaskMapper taskMapper;

    @GetMapping("/tasks")
    public List<Task> getAll() {
        return repository.getAllByOwner(details.getCurrentUser());
    }

    @GetMapping("/tasks/finished")
    public List<Task> getFinished() {
        return repository.getAllByOwnerAndFinishedTimeNotNull(details.getCurrentUser());
    }

    @GetMapping("/tasks/unfinished")
    public List<Task> getUnfinished() {
        return repository.getAllByOwnerAndFinishedTimeNull(details.getCurrentUser());
    }

    @GetMapping("/tasks/unfinished/{date}")
    public List<Task> getUnfinishedByDate(@PathVariable String date) {
        if (date == null || date.isEmpty()) {
            return getUnfinished();
        }
        try {
            Instant from = Instant.parse(date);
            Instant to = from.plus(1, DAYS);
            return repository.getAllByOwnerAndFinishedTimeBetween(details.getCurrentUser(), from, to);
        } catch (DateTimeParseException e) {
            throw new InvalidDateParameterException();
        }
    }

    @PostMapping("/task")
    public Task add(@RequestBody TaskDTO dto) {
        Task newTask = taskMapper.toEntity(dto);
        newTask.setOwner(details.getCurrentUser());
        return repository.save(newTask);
    }

    @DeleteMapping("/task")
    public void delete(@RequestBody TaskDTO dto) {
        repository.delete(repository.getTaskByUuidAndOwnerOrThrow(dto.uuid(), details.getCurrentUser()));
    }

    @PutMapping("/task")
    public Task update(@RequestBody TaskDTO dto) {
        Task task = taskMapper.toEntity(dto);
        task.setOwner(details.getCurrentUser());
        return repository.save(task);
    }

    @PatchMapping("/task/title")
    public Task updateTitle(@RequestBody TaskDTO dto) {
        Task task = repository.getTaskByUuidAndOwnerOrThrow(dto.uuid(), details.getCurrentUser());
        task.setTitle(dto.title());
        return repository.save(task);
    }

    @PatchMapping("/task/text")
    public Task updateText(@RequestBody TaskDTO dto) {
        Task task = repository.getTaskByUuidAndOwnerOrThrow(dto.uuid(), details.getCurrentUser());
        task.setText(dto.text());
        return repository.save(task);
    }

    @PatchMapping("/task/finish")
    public Task finish(@RequestBody TaskDTO dto) {
        Task task = repository.getTaskByUuidAndOwnerOrThrow(dto.uuid(),
                details.getCurrentUser());
        task.setFinishedTime(Instant.now());
        return repository.save(task);
    }

    @PatchMapping("/task/continue")
    public Task continueFinished(@RequestBody TaskDTO dto) {
        Task task = repository.getTaskByUuidAndOwnerOrThrow(dto.uuid(),
                details.getCurrentUser());
        task.setFinishedTime(null);
        return repository.save(task);
    }

}
