package org.roadmap.tasktrackerbackend.controller;

import lombok.RequiredArgsConstructor;
import org.roadmap.tasktrackerbackend.model.Task;
import org.roadmap.tasktrackerbackend.repository.TaskRepository;
import org.roadmap.tasktrackerbackend.security.CurrentUserAuthorizationDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskRepository repository;

    private final CurrentUserAuthorizationDetails details;

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

    @PostMapping(value = "/task",
            consumes = {"application/x-www-form-urlencoded;charset=UTF-8", "application/json"})
    public Task add(String title, String text) {
        return repository.save(new Task(title, text, details.getCurrentUser()));
    }

    @DeleteMapping(value = "/task",
            consumes = {"application/x-www-form-urlencoded;charset=UTF-8", "application/json"})
    public void delete(UUID uuid) {
        repository.delete(repository.getTaskByUuidAndOwnerOrThrow(uuid, details.getCurrentUser()));
    }

    @PatchMapping(value = "/task/title",
            consumes = {"application/x-www-form-urlencoded;charset=UTF-8", "application/json"})
    public Task updateTitle(UUID uuid, String newTitle) {
        Task task = repository.getTaskByUuidAndOwnerOrThrow(uuid, details.getCurrentUser());
        task.setTitle(newTitle);
        return repository.save(task);
    }

    @PatchMapping(value = "/task/text",
            consumes = {"application/x-www-form-urlencoded;charset=UTF-8", "application/json"})
    public Task updateText(UUID uuid, String newText) {
        Task task = repository.getTaskByUuidAndOwnerOrThrow(uuid, details.getCurrentUser());
        task.setText(newText);
        return repository.save(task);
    }

    @PatchMapping(value = "/task/finish",
            consumes = {"application/x-www-form-urlencoded;charset=UTF-8", "application/json"})
    public Task finish(UUID uuid) {
        Task task = repository.getTaskByUuidAndOwnerOrThrow(uuid, details.getCurrentUser());
        task.setFinishedTime(Instant.now());
        return repository.save(task);
    }


}
