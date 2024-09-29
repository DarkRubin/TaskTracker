package org.roadmap.tasktrackerbackend.controller;

import lombok.RequiredArgsConstructor;
import org.roadmap.tasktrackerbackend.dto.TaskDTO;
import org.roadmap.tasktrackerbackend.exception.UserNotAuthorizedException;
import org.roadmap.tasktrackerbackend.model.User;
import org.roadmap.tasktrackerbackend.repository.TaskRepository;
import org.roadmap.tasktrackerbackend.repository.UserRepository;
import org.roadmap.tasktrackerbackend.service.TaskService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskRepository repository;

    private final TaskService service;

    private final UserRepository userRepository;

    private User getCurrentUser() {
        var email = getContext().getAuthentication().getName();
        var password = getContext().getAuthentication().getCredentials();
        return userRepository.findByEmailAndPassword(email, password.toString())
                .orElseThrow(UserNotAuthorizedException::new);
    }

    @GetMapping("/tasks")
    public List<TaskDTO> getAll() {
        return service.getTasks(getCurrentUser());
    }

    @GetMapping("/tasks/finished")
    public List<TaskDTO> getFinished() {
        return service.getFinished(getCurrentUser());
    }

    @PostMapping(value = "/task",
            consumes = {"application/x-www-form-urlencoded;charset=UTF-8", "application/json"})
    public TaskDTO add(String title, String text) {
        return service.save(title, text, getCurrentUser());
    }

    @DeleteMapping(value = "/task",
            consumes = {"application/x-www-form-urlencoded;charset=UTF-8", "application/json"})
    public void delete(UUID uuid) {
        repository.deleteByUuid(uuid);
    }

    @PatchMapping(value = "/task/title",
            consumes = {"application/x-www-form-urlencoded;charset=UTF-8", "application/json"})
    public void updateTitle(UUID uuid, String newTitle) {
        service.updateTitle(uuid, newTitle);
    }

    @PatchMapping(value = "/task/text",
            consumes = {"application/x-www-form-urlencoded;charset=UTF-8", "application/json"})
    public void updateText(UUID uuid, String newText) {
        service.updateText(uuid, newText);
    }

    @PatchMapping(value = "/task/finish",
            consumes = {"application/x-www-form-urlencoded;charset=UTF-8", "application/json"})
    public void finish(UUID uuid) {
        service.finishTask(uuid);
    }


}
